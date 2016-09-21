package ru.android.shiz.ra.broadcastpreview.camera;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ru.android.shiz.ra.R;

/**
 * Created by Tinghan_Chang on 2016/2/2.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CamBaseV2 {

    private static String LOG_TAG = CamBaseV2.class.getSimpleName();
    private Activity mApp = null;
    private CameraDevice mCamera = null;
    private CameraManager mCameraManager = null;
    private CameraCharacteristics mCameraCharacteristics = null;
    private CameraCaptureSession mPreviewSession = null;
    private CaptureRequest.Builder mPreviewBuilder = null;
    private String[] mCameraId = null;
    private HandlerThread mCameraThread = null;
    private Handler mCameraHandler = null;
    private Surface mPreviewSurface = null;
    private boolean mIsPreviewing = false;
    private RelativeLayout rootView = null;
    private Size mPreviewSize = null;
    private PreviewGLSurfaceView mPreviewSurfaceView = null;
    private SurfaceTexture mPreviewSurfaceTexture = null;
    private boolean mIsFullDeviceHeight = false;

    private Context context = null;

    public CamBaseV2(Activity app, RelativeLayout rootView) {
        mApp = app;
        this.rootView = rootView;
    }

    public CamBaseV2(Context context, RelativeLayout rootView) {
        this.context = context;
        this.rootView = rootView;
    }

    public void onActivityResume() {
        Log.e(LOG_TAG, "LifeCycle, onActivityResume");
        initCameraThread();
        openCamera();
    }

    private void initCameraThread() {
        Log.e(LOG_TAG, "init camera thread begin.");
        mCameraThread = new HandlerThread("Camera Handler Thread");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
        Log.e(LOG_TAG, "init camera thread done");
    }

    public void onActivityPause() {
        Log.e(LOG_TAG, "LifeCycle, onActivityPause");
        releaseCamera();
        releaseCameraThread();
        releaseSurfaceView();
        Log.e(LOG_TAG, "LifeCycle, onActivityPause done");
    }

    private void releaseCamera() {
        // release camera
        if (mPreviewSession != null) {
            try {
                mPreviewSession.stopRepeating();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            mPreviewSession.close();
            mPreviewSession = null;
            mIsPreviewing = false;
        }
        if (mCamera != null) {
            mCamera.close();
            mCamera = null;
        }
    }

    private void releaseCameraThread() {
        if (mCameraThread != null) {
            mCameraThread.interrupt();
            mCameraThread = null;
        }
        if (mCameraHandler != null) {
            mCameraHandler = null;
        }
    }

    private void releaseSurfaceView() {
        if (mPreviewSurface != null) {
            rootView.removeView(mPreviewSurfaceView);
            mPreviewSurfaceTexture = null;
            mPreviewSurface = null;
        }
    }

    private void openCamera() {
//        mCameraManager = (CameraManager) mApp.getSystemService(Context.CAMERA_SERVICE);
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList();
            mCameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraId[0]);

            // Because camera2.0 only can control view size.
            // So we need to dynamic create view to fit sensor size.
            createSurfaceView(rootView);
            Log.e(LOG_TAG, "camera open begin");
            mCameraManager.openCamera(mCameraId[0], mCameraDeviceStateCallback, mCameraHandler);
        } catch (CameraAccessException | SecurityException e) {
            e.printStackTrace();
        }
    }

    private void createSurfaceView(RelativeLayout rootLayout) {
        LinearLayout.LayoutParams layoutParams = getPreviewLayoutParams();

        Log.d(LOG_TAG, "layoutParams. w: " + layoutParams.width + ", h: " + layoutParams.height);
        Log.d(LOG_TAG, "rotation: " + getDisplayRotation(context));

//        mPreviewSize = new Size(layoutParams.width, layoutParams.height);
        int displayRotation = getDisplayRotation(context);
        if (displayRotation == 0  || displayRotation == 180) {
            int tmp = layoutParams.width;
            layoutParams.width = layoutParams.height;
            layoutParams.height = tmp;
        }


        mPreviewSurfaceView = new PreviewGLSurfaceView(context, mPreviewSize);
        mPreviewSurfaceView.setLayoutParams(layoutParams);
        mPreviewSurfaceView.setSurfaceTextureListener(mSurfaceextureListener);

        rootLayout.addView(mPreviewSurfaceView);
    }

    private LinearLayout.LayoutParams getPreviewLayoutParams() {
        Point screenSize = new Point();
//        mApp.getWindowManager().getDefaultDisplay().getSize(screenSize);

        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(screenSize);

        Rect activeArea = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        int sensorOrientation = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        int sensorWidth, sensorHeight, previewWidth, previewHeight;
        // Make sensor's orientation same as screen.
        switch (sensorOrientation) {
            case 90:
            case 180:
                sensorWidth = activeArea.height();
                sensorHeight = activeArea.width();
                break;
            case 270:
            case 0:
            default:
                sensorWidth = activeArea.width();
                sensorHeight = activeArea.height();
                break;
        }
        Log.i(LOG_TAG, "Sensor Orientation angle:" + sensorOrientation);
        Log.i(LOG_TAG, "Sensor Width/Height : " + sensorWidth + "/" + sensorHeight);
        Log.i(LOG_TAG, "Screen Width/Height : " + screenSize.x + "/" + screenSize.y);

        StreamConfigurationMap map = mCameraCharacteristics
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        Size[] choices = map.getOutputSizes(SurfaceTexture.class);

        previewWidth = choices[choices.length - 1].getWidth();
        previewHeight = choices[choices.length - 1].getHeight();
        mPreviewSize = new Size(previewWidth, previewHeight);

        Log.d(LOG_TAG, "previewWidth: " + previewWidth + ", previewHeight: " + previewHeight);

        // Set margin to center at screen.
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(choices[0].getWidth(), choices[0].getHeight());

        // Preview's View size must same as sensor ratio.
//        if (screenSize.x < screenSize.y) {
////            previewWidth = screenSize.x;
////            previewHeight = screenSize.y + 8;
//            int widthMargin = (previewWidth - screenSize.y) / 2;
//            int heightMargin = (previewHeight - screenSize.x) / 2;
//            layoutParams.leftMargin = -widthMargin;
//            layoutParams.topMargin = -heightMargin;
//        } else {
////            previewWidth = screenSize.y;
////            previewHeight = screenSize.x + 8;
//            int widthMargin = (previewWidth - screenSize.x) / 2;
//            int heightMargin = (previewHeight - screenSize.y) / 2;
//            layoutParams.leftMargin = -widthMargin;
//            layoutParams.topMargin = -heightMargin;
//        }
        return layoutParams;
    }

    private int getDisplayRotation(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    private PreviewGLSurfaceView.SurfaceTextureListener mSurfaceextureListener = new PreviewGLSurfaceView.SurfaceTextureListener() {
        public void onSurfaceTextureAvailable(SurfaceTexture surface) {
            mPreviewSurfaceTexture = surface;
            mPreviewSurface = new Surface(mPreviewSurfaceTexture);
            startPreview();
        }
    };

    private CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.e(LOG_TAG, "camera open done");
            mCamera = camera;
            startPreview();
        }

        @Override
        public void onClosed(@NonNull CameraDevice camera) {
            mCamera = null;
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {

        }
    };

    /**
     * Maybe need to sync Camera and SurfaceView.
     * Maybe need to create SurfaceView after get camera size.
     */
    private void startPreview() {
        Log.e(LOG_TAG, "Try start preview.");
        if (mCamera != null && mPreviewSurface != null && !mIsPreviewing) {
            mIsPreviewing = true;
            List<Surface> outputSurfaces = new ArrayList<>(1);
            outputSurfaces.add(mPreviewSurface);
            try {
                Log.e(LOG_TAG, "createCaptureSession begin");
                mCamera.createCaptureSession(outputSurfaces, mPreviewSessionCallback, mCameraHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else if (mPreviewSurface == null) {
            Log.e(LOG_TAG, "mPreviewSurface is null");
        } else if (mCamera == null) {
            Log.e(LOG_TAG, "mCamera is null");
        } else if (mIsPreviewing) {
            Log.e(LOG_TAG, "mIsPreviewing");
        }
    }

    private CameraCaptureSession.StateCallback mPreviewSessionCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            Log.e(LOG_TAG, "createCaptureSession done");
            mPreviewSession = session;
            CaptureRequest.Builder previewBuilder = getPreviewBuilder();
            CaptureRequest request = previewBuilder.build();
            try {
                Log.e(LOG_TAG, "setRepeatingRequest begin");
                mPreviewSession.setRepeatingRequest(request, null, mCameraHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Log.d(LOG_TAG, "Session: " + session);
        }
    };

    private CaptureRequest.Builder getPreviewBuilder() {
        try {
            mPreviewBuilder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        mPreviewBuilder.addTarget(mPreviewSurface);
        return mPreviewBuilder;
    }
}
