package ru.android.shiz.ra.label;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import ru.android.shiz.ra.base.presenter.BaseRxLcePresenter;
import ru.android.shiz.ra.model.event.StreamLabelChangedEvent;
import ru.android.shiz.ra.model.stream.Label;
import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.model.stream.StreamProvider;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kassava on 22.08.2016.
 */
public class LabelPresenter extends BaseRxLcePresenter<LabelView, List<Label>> {

    private EventBus eventBus;
    private StreamProvider streamProvider;

    @Inject
    public LabelPresenter(EventBus eventBus, StreamProvider mailProvider) {
        this.eventBus = eventBus;
        this.streamProvider = mailProvider;
    }

    @Override
    public void attachView(LabelView view) {
        super.attachView(view);
        eventBus.register(this);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        eventBus.unregister(this);
    }

    public void loadLabels() {
        subscribe(streamProvider.getLabels(), false);
    }

    public void setLabel(final Stream stream, String newLabel) {

        // Optimistic propagation
        final String oldLabel = stream.getLabel();
        eventBus.post(new StreamLabelChangedEvent(stream, newLabel));

        streamProvider.setLabel(stream, newLabel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Stream>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new StreamLabelChangedEvent(stream, oldLabel));
                        if (isViewAttached()) {
                            getView().changeLabel(stream, oldLabel);
                            getView().showChangeLabelFailed(stream, e);
                        }
                    }

                    @Override
                    public void onNext(Stream s) {
                    }
                });

        // Don't cancel this onDetach

    }

    @Subscribe
    public void onEventMainThread(StreamLabelChangedEvent e) {
        if (isViewAttached()) {
            getView().changeLabel(e.getStream(), e.getLabel());
        }
    }
}
