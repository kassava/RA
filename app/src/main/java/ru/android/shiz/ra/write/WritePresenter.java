package ru.android.shiz.ra.write;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import ru.android.shiz.ra.IntentStarter;
import ru.android.shiz.ra.model.event.LoginSuccessfulEvent;
import ru.android.shiz.ra.model.event.NotAuthenticatedEvent;
import ru.android.shiz.ra.model.event.StreamSentErrorEvent;
import ru.android.shiz.ra.model.event.StreamSentEvent;
import ru.android.shiz.ra.model.stream.Stream;

/**
 * Created by kassava on 25.08.2016.
 */
public class WritePresenter extends MvpBasePresenter<WriteView> {

    private EventBus eventBus;
    private IntentStarter intentStarter;

    @Inject
    public WritePresenter(EventBus eventBus, IntentStarter intentStarter) {
        this.eventBus = eventBus;
        this.intentStarter = intentStarter;
    }

    public void writeMail(Context context, Stream stream) {
        getView().showLoading();
        intentStarter.sendStreamViaService(context, stream);
    }


    public void onEventMainThread(NotAuthenticatedEvent event) {
        if (isViewAttached()) {
            getView().showAuthenticationRequired();
        }
    }

    public void onEventMainThread(LoginSuccessfulEvent event) {
        if (isViewAttached()) {
            getView().showForm();
        }
    }

    public void onEventMainThread(StreamSentErrorEvent errorEvent){
        if (isViewAttached()){
            getView().showError(errorEvent.getException());
        }
    }

    @Subscribe
    public void onEventMainThread(StreamSentEvent event){
        if (isViewAttached()){
            getView().finishBecauseSuccessful();
        }
    }



    @Override
    public void attachView(WriteView view) {
        super.attachView(view);
        eventBus.register(this);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        eventBus.unregister(this);
    }
}
