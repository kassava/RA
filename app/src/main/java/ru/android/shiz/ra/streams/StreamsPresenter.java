package ru.android.shiz.ra.streams;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import ru.android.shiz.ra.base.presenter.BaseRxStreamPresenter;
import ru.android.shiz.ra.model.event.StreamLabelChangedEvent;
import ru.android.shiz.ra.model.event.StreamReceivedEvent;
import ru.android.shiz.ra.model.event.StreamSentEvent;
import ru.android.shiz.ra.model.stream.Label;
import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.model.stream.StreamProvider;

/**
 * Created by kassava on 28.04.2016.
 */
public class StreamsPresenter extends BaseRxStreamPresenter<StreamsView, List<Stream>> {

    @Inject public StreamsPresenter(StreamProvider streamProvider, EventBus eventBus) {
        super(streamProvider, eventBus);
    }

    public void load(boolean pullToRefresh, Label label) {
        subscribe(streamProvider.getStreamsOfLabel(label.getName()), pullToRefresh);
    }

    public void onEventMainThread(StreamSentEvent event) {
        onEventMainThread(new StreamLabelChangedEvent(event.getStream(), event.getStream().getLabel()));
    }

    public void onEventMainThread(StreamReceivedEvent event) {
        onEventMainThread(new StreamLabelChangedEvent(event.getStream(), event.getStream().getLabel()));
    }

    public void onEventMainThread(StreamLabelChangedEvent event) {
        if (isViewAttached()) {
            getView().changeLabel(event.getStream(), event.getLabel());
        }
    }
}
