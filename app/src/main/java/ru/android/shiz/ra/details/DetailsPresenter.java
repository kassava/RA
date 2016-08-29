package ru.android.shiz.ra.details;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ru.android.shiz.ra.base.presenter.BaseRxStreamPresenter;
import ru.android.shiz.ra.model.event.StreamReadEvent;
import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.model.stream.StreamProvider;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kassava on 22.08.2016.
 */
public class DetailsPresenter extends BaseRxStreamPresenter<DetailsView, Stream> {

    @Inject
    public DetailsPresenter(StreamProvider streamProvider, EventBus eventBus) {
        super(streamProvider, eventBus);
    }

    public void loadStream(int id) {
        subscribe(streamProvider.getStream(id), false);
    }

    @Override
    protected void onNext(Stream data) {
        super.onNext(data);
        if (isViewAttached() && !data.isRead()) {
            markAsRead(data);
        }
    }

    private void markAsRead(final Stream stream) {

        // We assume that this call could never fail
        eventBus.post(new StreamReadEvent(stream, true));
        streamProvider.markAsRead(stream, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
