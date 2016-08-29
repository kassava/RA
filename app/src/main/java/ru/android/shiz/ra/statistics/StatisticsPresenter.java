package ru.android.shiz.ra.statistics;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ru.android.shiz.ra.base.presenter.BaseRxAuthPresenter;
import ru.android.shiz.ra.model.stream.StreamProvider;
import ru.android.shiz.ra.model.stream.statistics.StreamStatistics;

/**
 * Created by kassava on 25.08.2016.
 */
public class StatisticsPresenter extends BaseRxAuthPresenter<StatisticsView, StreamStatistics> {

    @Inject
    public StatisticsPresenter(StreamProvider streamProvider, EventBus eventBus) {
        super(streamProvider, eventBus);
    }

    public void loadStatistics() {
        subscribe(streamProvider.getStatistics(), false);
    }
}
