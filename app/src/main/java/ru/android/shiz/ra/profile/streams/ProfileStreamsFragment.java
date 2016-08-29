package ru.android.shiz.ra.profile.streams;

import com.hannesdorfmann.fragmentargs.annotation.Arg;

import ru.android.shiz.ra.RaApp;
import ru.android.shiz.ra.base.view.BaseStreamsFragment;
import ru.android.shiz.ra.dagger.NavigationModule;
import ru.android.shiz.ra.model.contact.Person;

/**
 * Created by kassava on 24.08.2016.
 */
public class ProfileStreamsFragment extends BaseStreamsFragment<ProfileStreamsView, ProfileStreamsPresenter>
        implements ProfileStreamsView {

    @Arg Person person;
    ProfileStreamsComponent profileStreamsComponent;

    @Override
    public ProfileStreamsPresenter createPresenter() {
        return profileStreamsComponent.presenter();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadStreamsSentBy(person, pullToRefresh);
    }

    @Override
    protected void injectDependencies() {
        profileStreamsComponent = DaggerProfileStreamsComponent.builder()
                .streamAppComponent(RaApp.getStreamComponents())
                .navigationModule(new NavigationModule())
                .build();

        profileStreamsComponent.inject(this);
    }
}
