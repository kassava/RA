package ru.android.shiz.ra.model.stream.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.model.stream.service.GcmFakeIntentService;

/**
 * Created by kassava on 24.08.2016.
 */
public class StreamReceiver extends BroadcastReceiver {

    public static final String ACTION_RECEIVE = "ru.android.shiz.ra.RECEIVE";
    public static final String EXTRA_STREAM = "ru.android.shiz.ra.STREAM_DATA";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_RECEIVE)) {
            Stream stream = intent.getParcelableExtra(EXTRA_STREAM);

            Intent gcmIntent = new Intent(context, GcmFakeIntentService.class);
            gcmIntent.putExtra(GcmFakeIntentService.KEY_STREAM, stream);
            context.startService(gcmIntent);
        }
    }
}
