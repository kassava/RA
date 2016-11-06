package ru.android.shiz.ra.api;

import android.util.Log;

import ru.android.shiz.ra.streaming.rtsp.RtspServer;

/**
 * Created by kassava on 18.10.2016.
 */

public class CustomRtspServer extends RtspServer {
    private final String LOG_TAG = "RTSP server";

    public CustomRtspServer() {
        super();
        // RTSP server disabled by default
//        mEnabled = false;

        Log.d(LOG_TAG, "customRtspServer");
    }
}
