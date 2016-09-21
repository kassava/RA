package ru.android.shiz.ra.broadcastpreview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.layout.MvpFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.RaApp;
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
    Size[] choices;
    @BindView(R.id.root_view) RelativeLayout rootView;
    @BindView(R.id.seekBar) SeekBar seekBar;
    @BindView(R.id.textView) TextView textView;

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

        camBaseV2 = new CamBaseV2(context, rootView);
        getCameraResolutions();
        int seekBarMax = choices.length - 1;
        seekBar.setMax(seekBarMax);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText(getResolutionStr(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBar.setProgress(0);
    }

    private String getResolutionStr(int number) {
        return new String("--- " + choices[number].getWidth() + " x " + choices[number].getHeight() + " ---");
    }

    @Nullable
    private void getCameraResolutions() {
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraId = cameraManager.getCameraIdList();
            CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId[0]);
            StreamConfigurationMap map = cameraCharacteristics
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            choices = map.getOutputSizes(SurfaceTexture.class);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
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