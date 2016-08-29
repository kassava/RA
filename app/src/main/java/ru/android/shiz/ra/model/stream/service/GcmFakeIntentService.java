package ru.android.shiz.ra.model.stream.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ru.android.shiz.ra.IntentStarter;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.RaApp;
import ru.android.shiz.ra.dagger.NavigationModule;
import ru.android.shiz.ra.model.event.StreamReceivedEvent;
import ru.android.shiz.ra.model.stream.Label;
import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.model.stream.StreamProvider;

/**
 * Simulates that a gcm push notification
 *
 * Created by kassava on 24.08.2016.
 */
public class GcmFakeIntentService extends IntentService {

    public static final String KEY_STREAM =
            "ru.android.shiz.ra.model.stream.service.FakeGcm.MAIL";

    @Inject
    StreamProvider streamProvider;
    @Inject
    EventBus eventBus;
    @Inject
    IntentStarter intentStarter;

    public GcmFakeIntentService() {
        super("GcmFakeIntentService");
        DaggerServiceComponent.builder()
                .streamAppComponent(RaApp.getStreamComponents())
                .navigationModule(new NavigationModule())
                .build()
                .inject(this);
    }

    @Override protected void onHandleIntent(Intent intent) {

        Stream stream = intent.getParcelableExtra(KEY_STREAM);

        // simulate network / receiving delay
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }

        stream.label(Label.INBOX);
        streamProvider.addStream(stream).subscribe();

        eventBus.post(new StreamReceivedEvent(stream));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

        Intent startIntent =
                intentStarter.getShowStreamInNewActivityIntent(getApplicationContext(), stream);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, startIntent, 0);
        builder.setContentIntent(pendingIntent);

        builder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                stream.getSender().getImageRes()));

        builder.setSmallIcon(R.drawable.ic_launcher)
                .setLights(getResources().getColor(R.color.primary), 1800, 3500)
                .setAutoCancel(true)
                .setContentTitle(stream.getSubject())
                .setContentText(stream.getText())
                .setWhen(stream.getDate().getTime())
                .setVibrate(new long[] { 1000, 1000 });

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(stream.getId(), builder.build());
    }
}
