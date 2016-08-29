package ru.android.shiz.ra.base.presenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ru.android.shiz.ra.base.view.BaseStreamView;
import ru.android.shiz.ra.model.event.StreamReadEvent;
import ru.android.shiz.ra.model.event.StreamStaredEvent;
import ru.android.shiz.ra.model.event.StreamUnstaredEvent;
import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.model.stream.StreamProvider;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Base Presenter implementation that already handles and listen
 * <ul>
 *   <li>Star a stream</li>
 *   <li>Unstar a stream</li>
 *   <li>mark stream as read</li>
 * Created by kassava on 21.08.2016.
 */
public class BaseRxStreamPresenter<V extends BaseStreamView<M>, M>
        extends BaseRxAuthPresenter<V, M> {

    @Inject public BaseRxStreamPresenter(StreamProvider streamProvider, EventBus eventBus) {
        super(streamProvider, eventBus);
    }

    public void starStream(final Stream stream, final boolean star) {
        // optimistic propagation
        if (star) {
            eventBus.post(new StreamStaredEvent(stream.getId()));
        } else {
            eventBus.post(new StreamStaredEvent(stream.getId()));
        }

        streamProvider.starStream(stream.getId(), star)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Stream>(){
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Oops, something went wrong, "undo"
                        if (star) {
                            eventBus.post(new StreamUnstaredEvent(stream.getId()));
                        } else {
                            eventBus.post(new StreamStaredEvent(stream.getId()));
                        }

                        if (isViewAttached()) {
                            if (star) {
                                getView().showStaringFailed(stream);
                            } else {
                                getView().showUnstaringFailed(stream);
                            }
                        }
                    }

                    @Override
                    public void onNext(Stream stream) {
                    }
                });
    }

    public void onEventMainThread(StreamStaredEvent event) {
        if (isViewAttached()) {
            getView().markStreamAsStared(event.getStreamId());
        }
    }

    public void onEventMainThread(StreamUnstaredEvent event) {
        if (isViewAttached()) {
            getView().markStreamAsUnstared(event.getStreamId());
        }
    }

    public void onEventMainThread(StreamReadEvent event) {
        if (isViewAttached()) {
            getView().markStreamAsRead(event.getStream(), event.isRead());
        }
    }
}
