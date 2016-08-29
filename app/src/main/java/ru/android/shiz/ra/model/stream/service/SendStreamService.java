package ru.android.shiz.ra.model.stream.service;

import android.app.IntentService;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ru.android.shiz.ra.RaApp;
import ru.android.shiz.ra.dagger.NavigationModule;
import ru.android.shiz.ra.model.account.AccountManager;
import ru.android.shiz.ra.model.event.StreamSentErrorEvent;
import ru.android.shiz.ra.model.event.StreamSentEvent;
import ru.android.shiz.ra.model.stream.Label;
import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.model.stream.StreamGenerator;
import ru.android.shiz.ra.model.stream.StreamProvider;
import ru.android.shiz.ra.model.stream.receiver.StreamReceiver;
import rx.Subscriber;

/**
 * Created by kassava on 24.08.2016.
 */
public class SendStreamService extends IntentService {

    public static final String KEY_STREAM =
            "ru.android.shiz.ra.model.stream.service.StreamingService.STREAM";

    @Inject
    StreamProvider streamProvider;
    @Inject
    EventBus eventBus;
    @Inject
    AccountManager accountManager;
    @Inject
    StreamGenerator generator;

    public SendStreamService() {
        super("MailingService");
        DaggerServiceComponent.builder()
                .streamAppComponent(RaApp.getStreamComponents())
                .navigationModule(new NavigationModule())
                .build()
                .inject(this);
    }

    @Override protected void onHandleIntent(Intent intent) {
        final Stream stream = intent.getParcelableExtra(KEY_STREAM);
        stream.label(Label.SENT);
        streamProvider.addStreamWithDelay(stream).subscribe(new Subscriber<Stream>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                eventBus.post(new StreamSentErrorEvent(stream, e));
            }

            @Override
            public void onNext(Stream stream) {
                eventBus.post(new StreamSentEvent(stream));
                generateResponse(stream);
            }
        });
    }

    private void generateResponse(Stream stream) {
        Stream response = generator.generateResponseStream(stream.getReceiver().getEstream());

        if (response != null) {
            response.subject("RE: " + stream.getSubject());
            Intent gcmIntent = new Intent();
            gcmIntent.setAction(StreamReceiver.ACTION_RECEIVE);
            gcmIntent.putExtra(StreamReceiver.EXTRA_STREAM, response);
            sendBroadcast(gcmIntent);
        }
    }
}
