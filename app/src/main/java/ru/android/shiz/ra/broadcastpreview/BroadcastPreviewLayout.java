package ru.android.shiz.ra.broadcastpreview;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hannesdorfmann.mosby.mvp.layout.MvpFrameLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import flow.Flow;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.RaApp;
import ru.android.shiz.ra.broadcastpreview.camera.AutoFitTextureView;
import ru.android.shiz.ra.broadcastpreview.camera.CamBaseV2;

/**
 * Created by kassava on 12.09.16.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BroadcastPreviewLayout extends MvpFrameLayout<BroadcastPreviewView, BroadcastPreviewPresenter>
        implements BroadcastPreviewView/*, View.OnClickListener*/ {

    private static final String LOG_TAG = BroadcastPreviewLayout.class.getSimpleName();

    private Context context;
    private CamBaseV2 camBaseV2 = null;
    @BindView(R.id.root_view) LinearLayout mRootView;

//    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
//    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
//    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
//    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();
//
//    private static final String TAG = "Camera2VideoFragment";
//    private static final int REQUEST_VIDEO_PERMISSIONS = 1;
//    private static final String FRAGMENT_DIALOG = "dialog";
//
//    private static final String[] VIDEO_PERMISSIONS = {
//            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO,
//    };
//
//    static {
//        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90);
//        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0);
//        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270);
//        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180);
//    }
//
//    static {
//        INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
//        INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
//        INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
//        INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);
//    }
//
//    /**
//     * An {@link AutoFitTextureView} for camera preview.
//     */
//    @BindView(R.id.texture)
//    AutoFitTextureView textureView;
//
//    /**
//     * Button to record video
//     */
//    @BindView(R.id.record_button) Button videoButton;
//
//    /**
//     * A refernce to the opened {@link android.hardware.camera2.CameraDevice}.
//     */
//    private CameraDevice cameraDevice;
//
//    /**
//     * A reference to the current {@link android.hardware.camera2.CameraCaptureSession} for
//     * preview.
//     */
//    private CameraCaptureSession previewSession;
//
//    /**
//     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a
//     * {@link TextureView}.
//     */
//    private TextureView.SurfaceTextureListener surfaceTextureListener
//            = new TextureView.SurfaceTextureListener() {
//
//        @Override
//        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture,
//                                              int width, int height) {
//            openCamera(width, height);
//        }
//
//        @Override
//        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture,
//                                                int width, int height) {
//            configureTransform(width, height);
//        }
//
//        @Override
//        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
//            return true;
//        }
//
//        @Override
//        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
//        }
//
//    };
//
//    /**
//     * The {@link android.util.Size} of camera preview.
//     */
//    private Size previewSize;
//
//    /**
//     * The {@link android.util.Size} of video recording.
//     */
//    private Size videoSize;
//
//    /**
//     * MediaRecorder
//     */
//    private MediaRecorder mediaRecorder;
//
//    /**
//     * Whether the app is recording video now
//     */
//    private boolean isRecordingVideo;
//
//    /**
//     * An additional thread for running tasks that shouldn't block the UI.
//     */
//    private HandlerThread backgroundThread;
//
//    /**
//     * A {@link Handler} for running tasks in the background.
//     */
//    private Handler backgroundHandler;
//
//    /**
//     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
//     */
//    private Semaphore cameraOpenCloseLock = new Semaphore(1);
//
//    /**
//     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its status.
//     */
//    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
//
//        @Override
//        public void onOpened(@NonNull CameraDevice cameraDevice) {
//            BroadcastPreviewLayout.this.cameraDevice = cameraDevice;
//            startPreview();
//            cameraOpenCloseLock.release();
//            if (null != textureView) {
//                configureTransform(textureView.getWidth(), textureView.getHeight());
//            }
//        }
//
//        @Override
//        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
//            cameraOpenCloseLock.release();
//            cameraDevice.close();
//            BroadcastPreviewLayout.this.cameraDevice = null;
//        }
//
//        @Override
//        public void onError(@NonNull CameraDevice cameraDevice, int error) {
//            cameraOpenCloseLock.release();
//            cameraDevice.close();
//            BroadcastPreviewLayout.this.cameraDevice = null;
//
////            Activity activity = getActivity();
////            if (null != activity) {
////                activity.finish();
////            }
//            if(context != null) {
//                Flow.get(context).goBack();
//            }
//        }
//
//    };
//    private Integer sensorOrientation;
//    private String nextVideoAbsolutePath;
//    private CaptureRequest.Builder previewBuilder;
//    private Surface recorderSurface;
//
////    public static Camera2VideoFragment newInstance() {
////        return new Camera2VideoFragment();
////    }
//
//    /**
//     * In this sample, we choose a video size with 3x4 aspect ratio. Also, we don't use sizes
//     * larger than 1080p, since MediaRecorder cannot handle such a high-resolution video.
//     *
//     * @param choices The list of available sizes
//     * @return The video size
//     */
//    private static Size chooseVideoSize(Size[] choices) {
//        for (Size size : choices) {
//            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
//                Log.d(LOG_TAG, "Size.width: " + size.getWidth() + ", Size.height: " + size.getHeight());
//                return size;
//            }
//        }
//        Log.e(TAG, "Couldn't find any suitable video size");
//        return choices[choices.length - 1];
//    }
//
//    /**
//     * Given {@code choices} of {@code Size}s supported by a camera, chooses the smallest one whose
//     * width and height are at least as large as the respective requested values, and whose aspect
//     * ratio matches with the specified value.
//     *
//     * @param choices     The list of sizes that the camera supports for the intended output class
//     * @param width       The minimum desired width
//     * @param height      The minimum desired height
//     * @param aspectRatio The aspect ratio
//     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
//     */
//    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
//        // Collect the supported resolutions that are at least as big as the preview Surface
//        List<Size> bigEnough = new ArrayList<>();
//        int w = aspectRatio.getWidth();
//        int h = aspectRatio.getHeight();
//        for (Size option : choices) {
//            if (option.getHeight() == option.getWidth() * h / w &&
//                    option.getWidth() >= width && option.getHeight() >= height) {
//                bigEnough.add(option);
//            }
//        }
//
//        // Pick the smallest of those, assuming we found any
//        if (bigEnough.size() > 0) {
//            return Collections.min(bigEnough, new CompareSizesByArea());
//        } else {
//            Log.e(TAG, "Couldn't find any suitable preview size");
//            return choices[0];
//        }
//    }
//
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        return inflater.inflate(R.layout.fragment_camera2_video, container, false);
////    }
////
////    @Override
////    public void onViewCreated(final View view, Bundle savedInstanceState) {
////        textureView = (AutoFitTextureView) view.findViewById(R.id.texture);
////        videoButton = (Button) view.findViewById(R.id.video);
////        videoButton.setOnClickListener(this);
////        view.findViewById(R.id.info).setOnClickListener(this);
////    }
//
////    @Override
////    public void onResume() {
////        super.onResume();
////        startBackgroundThread();
////        if (textureView.isAvailable()) {
////            openCamera(textureView.getWidth(), textureView.getHeight());
////        } else {
////            textureView.setSurfaceTextureListener(surfaceTextureListener);
////        }
////    }
//
////    @Override
////    public void onPause() {
////        closeCamera();
////        stopBackgroundThread();
////        super.onPause();
////    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.record_button: {
//                if (isRecordingVideo) {
//                    stopRecordingVideo();
//                } else {
//                    startRecordingVideo();
//                }
//                break;
//            }
//        }
//    }
//
//    /**
//     * Starts a background thread and its {@link Handler}.
//     */
//    private void startBackgroundThread() {
//        backgroundThread = new HandlerThread("CameraBackground");
//        backgroundThread.start();
//        backgroundHandler = new Handler(backgroundThread.getLooper());
//    }
//
//    /**
//     * Stops the background thread and its {@link Handler}.
//     */
//    private void stopBackgroundThread() {
//        backgroundThread.quitSafely();
//        try {
//            backgroundThread.join();
//            backgroundThread = null;
//            backgroundHandler = null;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Gets whether you should show UI with rationale for requesting permissions.
//     *
//     * @param permissions The permissions your app wants to request.
//     * @return Whether you can show permission rationale UI.
//     */
////    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
////        for (String permission : permissions) {
////            if (FragmentCompat.shouldShowRequestPermissionRationale(this, permission)) {
////                return true;
////            }
////        }
////        return false;
////    }
//
//    /**
//     * Requests permissions needed for recording video.
//     */
////    private void requestVideoPermissions() {
////        if (shouldShowRequestPermissionRationale(VIDEO_PERMISSIONS)) {
////            new ConfirmationDialog().show(getChildFragmentManager(), FRAGMENT_DIALOG);
////        } else {
////            FragmentCompat.requestPermissions(this, VIDEO_PERMISSIONS, REQUEST_VIDEO_PERMISSIONS);
////        }
////    }
////
////    @Override
////    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
////                                           @NonNull int[] grantResults) {
////        Log.d(LOG_TAG, "onRequestPermissionsResult");
////        if (requestCode == REQUEST_VIDEO_PERMISSIONS) {
////            if (grantResults.length == VIDEO_PERMISSIONS.length) {
////                for (int result : grantResults) {
////                    if (result != PackageManager.PERMISSION_GRANTED) {
////                        ErrorDialog.newInstance(getString(R.string.permission_request))
////                                .show(getChildFragmentManager(), FRAGMENT_DIALOG);
////                        break;
////                    }
////                }
////            } else {
////                ErrorDialog.newInstance(getString(R.string.permission_request))
////                        .show(getChildFragmentManager(), FRAGMENT_DIALOG);
////            }
////        } else {
////            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
////        }
////    }
////
////    private boolean hasPermissionsGranted(String[] permissions) {
////        for (String permission : permissions) {
////            if (ActivityCompat.checkSelfPermission(getActivity(), permission)
////                    != PackageManager.PERMISSION_GRANTED) {
////                return false;
////            }
////        }
////        return true;
////    }
//
//    /**
//     * Tries to open a {@link CameraDevice}. The result is listened by `stateCallback`.
//     */
//    private void openCamera(int width, int height) {
////        if (!hasPermissionsGranted(VIDEO_PERMISSIONS)) {
////            requestVideoPermissions();
////            return;
////        }
////        final Activity activity = getActivity();
////        if (null == activity || activity.isFinishing()) {
////            return;
////        }
//        if (context == null) {
//            return;
//        }
//        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
//        try {
//            Log.d(TAG, "tryAcquire");
//            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
//                throw new RuntimeException("Time out waiting to lock camera opening.");
//            }
//            String cameraId = manager.getCameraIdList()[0];
//
//            // Choose the sizes for camera preview and video recording
//            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
//            StreamConfigurationMap map = characteristics
//                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//            sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
//            videoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
//            previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
//                    width, height, videoSize);
//
//            int orientation = getResources().getConfiguration().orientation;
//            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                textureView.setAspectRatio(previewSize.getWidth(), previewSize.getHeight());
//            } else {
//                textureView.setAspectRatio(previewSize.getHeight(), previewSize.getWidth());
//            }
//            configureTransform(width, height);
//            mediaRecorder = new MediaRecorder();
//            manager.openCamera(cameraId, stateCallback, null);
//        } catch (CameraAccessException e) {
////            Toast.makeText(activity, "Cannot access the camera.", Toast.LENGTH_SHORT).show();
////            activity.finish();
//            Log.d(LOG_TAG, "Не достпуа к камере.");
//        } catch (NullPointerException e) {
//            // Currently an NPE is thrown when the Camera2API is used but not supported on the
//            // device this code runs.
////            ErrorDialog.newInstance(getString(R.string.camera_error))
////                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
//            Log.d(LOG_TAG, "Это устройство не поддерживает Camera2 API");
//        } catch (InterruptedException e) {
//            throw new RuntimeException("Interrupted while trying to lock camera opening.");
//        }
//    }
//
//    private void closeCamera() {
//        try {
//            cameraOpenCloseLock.acquire();
//            closePreviewSession();
//            if (null != cameraDevice) {
//                cameraDevice.close();
//                cameraDevice = null;
//            }
//            if (null != mediaRecorder) {
//                mediaRecorder.release();
//                mediaRecorder = null;
//            }
//        } catch (InterruptedException e) {
//            throw new RuntimeException("Interrupted while trying to lock camera closing.");
//        } finally {
//            cameraOpenCloseLock.release();
//        }
//    }
//
//    /**
//     * Start the camera preview.
//     */
//    private void startPreview() {
//        if (null == cameraDevice || !textureView.isAvailable() || null == previewSize) {
//            return;
//        }
//        try {
//            closePreviewSession();
//            SurfaceTexture texture = textureView.getSurfaceTexture();
//            assert texture != null;
//            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
//            previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//
//            Surface previewSurface = new Surface(texture);
//            previewBuilder.addTarget(previewSurface);
//
//            cameraDevice.createCaptureSession(Arrays.asList(previewSurface), new CameraCaptureSession.StateCallback() {
//
//                @Override
//                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
//                    previewSession = cameraCaptureSession;
//                    updatePreview();
//                }
//
//                @Override
//                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
////                    Activity activity = getActivity();
////                    if (null != activity) {
////                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
////                    }
//                    Log.d(LOG_TAG, "Failed");
//                }
//            }, backgroundHandler);
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Update the camera preview. {@link #startPreview()} needs to be called in advance.
//     */
//    private void updatePreview() {
//        if (null == cameraDevice) {
//            return;
//        }
//        try {
//            setUpCaptureRequestBuilder(previewBuilder);
//            HandlerThread thread = new HandlerThread("CameraPreview");
//            thread.start();
//            previewSession.setRepeatingRequest(previewBuilder.build(), null, backgroundHandler);
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder) {
//        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
//    }
//
//    /**
//     * Configures the necessary {@link android.graphics.Matrix} transformation to `textureView`.
//     * This method should not to be called until the camera preview size is determined in
//     * openCamera, or until the size of `textureView` is fixed.
//     *
//     * @param viewWidth  The width of `textureView`
//     * @param viewHeight The height of `textureView`
//     */
//    private void configureTransform(int viewWidth, int viewHeight) {
////        Activity activity = getActivity();
//        if (null == textureView || null == previewSize || null == context) {
//            return;
//        }
////        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//        int rotation = display.getRotation();
//
//        Matrix matrix = new Matrix();
//        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
//        RectF bufferRect = new RectF(0, 0, previewSize.getHeight(), previewSize.getWidth());
//        float centerX = viewRect.centerX();
//        float centerY = viewRect.centerY();
//        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
//            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
//            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
//            float scale = Math.max(
//                    (float) viewHeight / previewSize.getHeight(),
//                    (float) viewWidth / previewSize.getWidth());
//            matrix.postScale(scale, scale, centerX, centerY);
//            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
//        }
//        textureView.setTransform(matrix);
//    }
//
//    private void setUpMediaRecorder() throws IOException {
////        final Activity activity = getActivity();
////        if (null == activity) {
////            return;
////        }
//        if(context == null) {
//            return;
//        }
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        if (nextVideoAbsolutePath == null || nextVideoAbsolutePath.isEmpty()) {
////            nextVideoAbsolutePath = getVideoFilePath(getActivity());
//            nextVideoAbsolutePath = getVideoFilePath(context);
//        }
//        mediaRecorder.setOutputFile(nextVideoAbsolutePath);
//        mediaRecorder.setVideoEncodingBitRate(10000000);
//        mediaRecorder.setVideoFrameRate(30);
//        mediaRecorder.setVideoSize(videoSize.getWidth(), videoSize.getHeight());
//        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
////        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//        int rotation = display.getRotation();
//
//        switch (sensorOrientation) {
//            case SENSOR_ORIENTATION_DEFAULT_DEGREES:
//                mediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
//                break;
//            case SENSOR_ORIENTATION_INVERSE_DEGREES:
//                mediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
//                break;
//        }
//        mediaRecorder.prepare();
//    }
//
//    private String getVideoFilePath(Context context) {
//        return context.getExternalFilesDir(null).getAbsolutePath() + "/"
//                + System.currentTimeMillis() + ".mp4";
//    }
//
//    private void startRecordingVideo() {
//        if (null == cameraDevice || !textureView.isAvailable() || null == previewSize) {
//            return;
//        }
//        try {
//            closePreviewSession();
//            setUpMediaRecorder();
//            SurfaceTexture texture = textureView.getSurfaceTexture();
//            assert texture != null;
//            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
//            previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
//            List<Surface> surfaces = new ArrayList<>();
//
//            // Set up Surface for the camera preview
//            Surface previewSurface = new Surface(texture);
//            surfaces.add(previewSurface);
//            previewBuilder.addTarget(previewSurface);
//
//            // Set up Surface for the MediaRecorder
//            recorderSurface = mediaRecorder.getSurface();
//            surfaces.add(recorderSurface);
//            previewBuilder.addTarget(recorderSurface);
//
//            // Start a capture session
//            // Once the session starts, we can update the UI and start recording
//            cameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
//
//                @Override
//                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
//                    previewSession = cameraCaptureSession;
//                    updatePreview();
//                    // Something strange...
//                    videoButton.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            // UI
//                            videoButton.setText(R.string.stop);
//                            isRecordingVideo = true;
//
//                            // Start recording
//                            mediaRecorder.start();
//                        }
//                    });
//                }
//
//                @Override
//                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
////                    Activity activity = getActivity();
////                    if (null != activity) {
////                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
////                    }
//                    Log.d(LOG_TAG, "Failed");
//                }
//            }, backgroundHandler);
//        } catch (CameraAccessException | IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void closePreviewSession() {
//        if (previewSession != null) {
//            previewSession.close();
//            previewSession = null;
//        }
//    }
//
//    private void stopRecordingVideo() {
//        // UI
//        isRecordingVideo = false;
//        videoButton.setText(R.string.record);
//        // Stop recording
//        mediaRecorder.stop();
//        mediaRecorder.reset();
//
////        Activity activity = getActivity();
////        if (null != activity) {
////            Toast.makeText(activity, "Video saved: " + nextVideoAbsolutePath,
////                    Toast.LENGTH_SHORT).show();
////            Log.d(LOG_TAG, "Video saved: " + nextVideoAbsolutePath);
////        }
//        if (context != null) {
//            Log.d(TAG, "Video saved: " + nextVideoAbsolutePath);
//        }
//        nextVideoAbsolutePath = null;
//        startPreview();
//    }
//
//    /**
//     * Compares two {@code Size}s based on their areas.
//     */
//    static class CompareSizesByArea implements Comparator<Size> {
//
//        @Override
//        public int compare(Size lhs, Size rhs) {
//            // We cast here to ensure the multiplications won't overflow
//            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
//                    (long) rhs.getWidth() * rhs.getHeight());
//        }
//
//    }
//
////    public static class ErrorDialog extends DialogFragment {
////
////        private static final String ARG_MESSAGE = "message";
////
////        public static ErrorDialog newInstance(String message) {
////            ErrorDialog dialog = new ErrorDialog();
////            Bundle args = new Bundle();
////            args.putString(ARG_MESSAGE, message);
////            dialog.setArguments(args);
////            return dialog;
////        }
////
////        @Override
////        public Dialog onCreateDialog(Bundle savedInstanceState) {
////            final Activity activity = getActivity();
////            return new AlertDialog.Builder(activity)
////                    .setMessage(getArguments().getString(ARG_MESSAGE))
////                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialogInterface, int i) {
////                            activity.finish();
////                        }
////                    })
////                    .create();
////        }
////
////    }
//
////    public static class ConfirmationDialog extends DialogFragment {
////
////        @Override
////        public Dialog onCreateDialog(Bundle savedInstanceState) {
////            final Fragment parent = getParentFragment();
////            return new AlertDialog.Builder(getActivity())
////                    .setMessage(R.string.permission_request)
////                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////                            FragmentCompat.requestPermissions(parent, VIDEO_PERMISSIONS,
////                                    REQUEST_VIDEO_PERMISSIONS);
////                        }
////                    })
////                    .setNegativeButton(android.R.string.cancel,
////                            new DialogInterface.OnClickListener() {
////                                @Override
////                                public void onClick(DialogInterface dialog, int which) {
////                                    parent.getActivity().finish();
////                                }
////                            })
////                    .create();
////        }
////
////    }

    public BroadcastPreviewLayout(Context ctx, AttributeSet attributeSet) {
        super(ctx, attributeSet);

        this.context = ctx;
    }

    @NonNull @Override
    public BroadcastPreviewPresenter createPresenter() {
        return RaApp.getComponent().broadcastPreviewPresenter();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        mRootView = (LinearLayout) findViewById(R.id.root_view);
        camBaseV2 = new CamBaseV2(context, mRootView);
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d(LOG_TAG, "onDetachedFromWindow");
        //        closeCamera();
//        stopBackgroundThread();
        super.onDetachedFromWindow();

        camBaseV2.onActivityPause();
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d(LOG_TAG, "onAttachedFromWindow");
        super.onAttachedToWindow();

//        startBackgroundThread();
//        if (textureView.isAvailable()) {
//            openCamera(textureView.getWidth(), textureView.getHeight());
//        } else {
//            textureView.setSurfaceTextureListener(surfaceTextureListener);
//        }

        camBaseV2.onActivityResume();
    }
}