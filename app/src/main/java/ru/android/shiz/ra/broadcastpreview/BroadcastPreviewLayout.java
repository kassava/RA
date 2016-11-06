package ru.android.shiz.ra.broadcastpreview;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
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
import ru.android.shiz.ra.api.CustomHttpServer;
import ru.android.shiz.ra.api.CustomRtspServer;
import ru.android.shiz.ra.broadcastpreview.camera.CamBaseV2;
import ru.android.shiz.ra.http.TinyHttpServer;
import ru.android.shiz.ra.streaming.SessionBuilder;
import ru.android.shiz.ra.streaming.gl.SurfaceView;
import ru.android.shiz.ra.streaming.rtsp.RtspServer;
import ru.android.shiz.ra.streaming.video.VideoQuality;

/**
 * Created by kassava on 12.09.16.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BroadcastPreviewLayout extends MvpFrameLayout<BroadcastPreviewView, BroadcastPreviewPresenter>
        implements BroadcastPreviewView/*, View.OnClickListener*/ {

    private static final String LOG_TAG = BroadcastPreviewLayout.class.getSimpleName();

    private final int HANDSET = 0x01;
    private final int TABLET = 0x02;

    // We assume that device is a phone.
    private int device = HANDSET;

    private RaApp raApp;
    private CustomHttpServer httpServer;
    private RtspServer rtspServer;
    private Context context;
    Size[] choices;
    @BindView(R.id.handset_camera_view) SurfaceView surfaceView;

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

        // Configures the SessionBuilder
        SessionBuilder.getInstance()
                .setSurfaceView(surfaceView)
                .setPreviewOrientation(90)
                .setContext(context.getApplicationContext())
                .setAudioEncoder(SessionBuilder.AUDIO_NONE)
                .setVideoEncoder(SessionBuilder.VIDEO_H264);

        // Starts the service of the HTTP server
        context.startService(new Intent(context, CustomHttpServer.class));

        // Starts the service of the RTSP server
        context.startService(new Intent(context, CustomRtspServer.class));
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
    protected void onAttachedToWindow() {
        Log.d(LOG_TAG, "onAttachedFromWindow: " + context.getApplicationContext());

        context.bindService(new Intent(context, CustomHttpServer.class), httpServiceConnection,
                Context.BIND_AUTO_CREATE);
        context.bindService(new Intent(context, CustomRtspServer.class), rtspServiceConnection,
                Context.BIND_AUTO_CREATE);

        super.onAttachedToWindow();

    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d(LOG_TAG, "onDetachedFromWindow");

        if (httpServer != null) {
            httpServer.removeCallbackListener(httpCallbackListener);
        }
        context.unbindService(httpServiceConnection);
        if (rtspServer != null) {
            rtspServer.removeCallbackListener(rtspCallbackListener);
        }
        context.unbindService(rtspServiceConnection);

        // Removes notification
//        if (raApp.notificationEnabled) removeNotification();
        // Kills HTTP server
        context.stopService(new Intent(context, CustomHttpServer.class));
        // Kills RTSP server
        context.stopService(new Intent(context, CustomRtspServer.class));
        // Returns to home menu

        super.onDetachedFromWindow();
    }

    private void removeNotification() {
        ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);
    }

    private ServiceConnection rtspServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            rtspServer = (CustomRtspServer) ((RtspServer.LocalBinder) service).getService();
            rtspServer.addCallbackListener(rtspCallbackListener);
            rtspServer.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private RtspServer.CallbackListener rtspCallbackListener = new RtspServer.CallbackListener() {
        @Override
        public void onError(RtspServer server, Exception e, int error) {
            // The port is already used by another app.
            if (error == RtspServer.ERROR_BIND_FAILED) {
                Log.d(LOG_TAG, "RTSP: Port is used, bind failed.");
            }
        }

        @Override
        public void onMessage(RtspServer server, int message) {
            if (message == RtspServer.MESSAGE_STREAMING_STARTED) {
                Log.d(LOG_TAG, "RTSP: streaming started.");
            } else if (message == RtspServer.MESSAGE_STREAMING_STOPPED) {
                Log.d(LOG_TAG, "RTSP: streaming stopped.");
            }
        }
    };

    private ServiceConnection httpServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            httpServer = (CustomHttpServer) ((TinyHttpServer.LocalBinder)service).getService();
            httpServer.addCallbackListener(httpCallbackListener);
            httpServer.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private TinyHttpServer.CallbackListener httpCallbackListener = new TinyHttpServer.CallbackListener() {

        @Override
        public void onError(TinyHttpServer server, Exception e, int error) {
            // We alert the user that the port is already used by another app.
            if (error == TinyHttpServer.ERROR_HTTP_BIND_FAILED ||
                    error == TinyHttpServer.ERROR_HTTPS_BIND_FAILED) {
                Log.d(LOG_TAG, "HTTP: port is used, bind failed.");
            }
        }

        @Override
        public void onMessage(TinyHttpServer server, int message) {
            if (message==CustomHttpServer.MESSAGE_STREAMING_STARTED) {
                Log.d(LOG_TAG, "HTTP: streaming started.");
            } else if (message==CustomHttpServer.MESSAGE_STREAMING_STOPPED) {
                Log.d(LOG_TAG, "HTTP: streaming stopped.");
            }
        }
    };
}