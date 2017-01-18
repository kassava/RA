package ru.android.shiz.ra;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import mortar.MortarScope;
import ru.android.shiz.ra.dagger.ApplicationComponent;
import ru.android.shiz.ra.streaming.SessionBuilder;
import ru.android.shiz.ra.streaming.video.VideoQuality;

/**
 * Created by kassava on 10.05.2016.
 */
public class RaApp extends Application {

    private static ApplicationComponent component;
    private MortarScope rootScope;

    public final static String TAG = "SpydroidApplication";

    /** Default quality of video streams. */
    public VideoQuality videoQuality = new VideoQuality(1280,720,16,500000);

    /** By default AMR is the audio encoder. */
    public int audioEncoder = SessionBuilder.AUDIO_AAC;

    /** By default H.263 is the video encoder. */
    public int videoEncoder = SessionBuilder.VIDEO_H264;

    /** Set this flag to true to disable the ads. */
    public final boolean DONATE_VERSION = false;

    /** If the notification is enabled in the status bar of the phone. */
    public boolean notificationEnabled = true;

    /** The HttpServer will use those variables to send reports about the state of the app to the web interface. */
    public boolean applicationForeground = true;
    public Exception lastCaughtException = null;

    /** Contains an approximation of the battery level. */
    public int batteryLevel = 0;

    private static RaApp raApp;

    @Override
    public void onCreate() {
        raApp = this;

        super.onCreate();
        component = ru.android.shiz.ra.dagger.DaggerApplicationComponent.create();

        initializeSession();

        registerReceiver(mBatteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    /**
     * Set desired params for session from shared preferences.
     */
    private void initializeSession() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        notificationEnabled = settings.getBoolean("notification_enabled", true);

        // On android 3.* AAC ADTS is not supported so we set the default encoder to AMR-NB, on android 4.* AAC is the default encoder
        audioEncoder = (Integer.parseInt(android.os.Build.VERSION.SDK) < 14) ? SessionBuilder.AUDIO_AMRNB : SessionBuilder.AUDIO_AAC;
        audioEncoder = Integer.parseInt(settings.getString("audio_encoder", String.valueOf(audioEncoder)));
        videoEncoder = Integer.parseInt(settings.getString("video_encoder", String.valueOf(videoEncoder)));

        // Read video quality settings from the preferences
        videoQuality = new VideoQuality(
                settings.getInt("video_resX", videoQuality.resX),
                settings.getInt("video_resY", videoQuality.resY),
                Integer.parseInt(settings.getString("video_framerate", String.valueOf(videoQuality.framerate))),
                Integer.parseInt(settings.getString("video_bitrate", String.valueOf(videoQuality.bitrate/1000)))*1000);

        SessionBuilder.getInstance()
                .setContext(getApplicationContext())
                .setAudioEncoder(!settings.getBoolean("stream_audio", true)?0:audioEncoder)
                .setVideoEncoder(!settings.getBoolean("stream_video", false)?0:videoEncoder)
                .setVideoQuality(videoQuality);

        // Listens to changes of preferences
        settings.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
    }

    public static RaApp getInstance() {
        return raApp;
    }

    @Override
    public Object getSystemService(String name) {
        if (rootScope == null) rootScope = MortarScope.buildRootScope().build("Root");

        return rootScope.hasService(name) ? rootScope.getService(name) : super.getSystemService(name);
    }

    public static ApplicationComponent getComponent() {
        return component;
    }

    private SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("video_resX") || key.equals("video_resY")) {
                videoQuality.resX = sharedPreferences.getInt("video_resX", 0);
                videoQuality.resY = sharedPreferences.getInt("video_resY", 0);
            }

            else if (key.equals("video_framerate")) {
                videoQuality.framerate = Integer.parseInt(sharedPreferences.getString("video_framerate", "0"));
            }

            else if (key.equals("video_bitrate")) {
                videoQuality.bitrate = Integer.parseInt(sharedPreferences.getString("video_bitrate", "0"))*1000;
            }

            else if (key.equals("audio_encoder") || key.equals("stream_audio")) {
                audioEncoder = Integer.parseInt(sharedPreferences.getString("audio_encoder", String.valueOf(audioEncoder)));
                SessionBuilder.getInstance().setAudioEncoder( audioEncoder );
                if (!sharedPreferences.getBoolean("stream_audio", false))
                    SessionBuilder.getInstance().setAudioEncoder(0);
            }

            else if (key.equals("stream_video") || key.equals("video_encoder")) {
                videoEncoder = Integer.parseInt(sharedPreferences.getString("video_encoder", String.valueOf(videoEncoder)));
                SessionBuilder.getInstance().setVideoEncoder( videoEncoder );
                if (!sharedPreferences.getBoolean("stream_video", true))
                    SessionBuilder.getInstance().setVideoEncoder(0);
            }

            else if (key.equals("notification_enabled")) {
                notificationEnabled  = sharedPreferences.getBoolean("notification_enabled", true);
            }

        }
    };

    private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            batteryLevel = intent.getIntExtra("level", 0);
        }
    };
}
