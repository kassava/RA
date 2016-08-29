package ru.android.shiz.ra.base.presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ru.android.shiz.ra.base.view.AuthView;
import ru.android.shiz.ra.model.account.NotAuthenticatedException;
import ru.android.shiz.ra.model.event.LoginSuccessfulEvent;
import ru.android.shiz.ra.model.event.NotAuthenticatedEvent;
import ru.android.shiz.ra.model.stream.StreamProvider;


/**
 * Created by kassava on 21.08.2016.
 */
public class BaseRxAuthPresenter<V extends AuthView<M>, M> extends BaseRxLcePresenter<V, M> {

    protected EventBus eventBus;
    protected StreamProvider streamProvider;

    public BaseRxAuthPresenter(StreamProvider streamProvider, EventBus eventBus) {
        this.eventBus = eventBus;
        this.streamProvider = streamProvider;
    }

    @Override
    protected void onError(Throwable e, boolean pullToRefresh) {
        if (e instanceof NotAuthenticatedException) {
            eventBus.post(new NotAuthenticatedEvent());
        } else {
            super.onError(e, pullToRefresh);
        }
    }

    @Subscribe
    public void onEventMainThread(NotAuthenticatedEvent event) {
        if (isViewAttached()) {
            getView().showAuthenticationRequired();
        }
    }

    @Subscribe
    public void onEventMainThread(LoginSuccessfulEvent event) {
        if (isViewAttached()) {
            getView().loadData(false);
        }
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
        eventBus.register(this);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        eventBus.unregister(this);
    }
}
