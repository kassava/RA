package ru.android.shiz.ra.menu;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import ru.android.shiz.ra.base.presenter.BaseRxAuthPresenter;
import ru.android.shiz.ra.model.event.LoginSuccessfulEvent;
import ru.android.shiz.ra.model.event.StreamReadEvent;
import ru.android.shiz.ra.model.stream.Label;
import ru.android.shiz.ra.model.stream.StreamProvider;

/**
 * Created by kassava on 24.08.2016.
 */
public class MenuPresenter extends BaseRxAuthPresenter<MenuView, List<Label>> {

    @Inject
    public MenuPresenter(StreamProvider streamProvider, EventBus eventBus) {
        super(streamProvider, eventBus);
    }

    public void loadLabels(boolean pullToRefresh) {
        subscribe(streamProvider.getLabels(), pullToRefresh);
    }

    public void onEventMainThread(LoginSuccessfulEvent event) {
        super.onEventMainThread(event);
        if (isViewAttached()){
            getView().setAccount(event.getAccount());
        }
    }

    public void onEventMainThread(StreamReadEvent event){
        if (isViewAttached()){
            String label = event.getStream().getLabel();
            getView().decrementUnreadCount(label);
        }
    }

}
