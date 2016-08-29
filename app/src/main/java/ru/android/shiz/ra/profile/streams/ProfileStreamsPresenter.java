package ru.android.shiz.ra.profile.streams;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import ru.android.shiz.ra.base.presenter.BaseRxStreamPresenter;
import ru.android.shiz.ra.model.contact.Person;
import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.model.stream.StreamProvider;

/**
 * Created by kassava on 24.08.2016.
 */
public class ProfileStreamsPresenter extends BaseRxStreamPresenter<ProfileStreamsView, List<Stream>> {

    @Inject
    public ProfileStreamsPresenter(StreamProvider streamProvider, EventBus eventBus) {
        super(streamProvider, eventBus);
    }

    public void loadStreamsSentBy(Person person, boolean pullToRefresh) {
        subscribe(streamProvider.getStreamsSentBy(person.getId()), pullToRefresh);
    }
}
