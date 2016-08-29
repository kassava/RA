package ru.android.shiz.ra.streams;

import javax.inject.Singleton;

import dagger.Component;
import ru.android.shiz.ra.dagger.NavigationModule;
import ru.android.shiz.ra.dagger.StreamAppComponent;
import ru.android.shiz.ra.dagger.StreamModule;

/**
 * Created by kassava on 22.08.2016.
 */
@Singleton
@Component(
        modules = {StreamModule.class, NavigationModule.class},
        dependencies = StreamAppComponent.class)
public interface StreamsComponent {

    public StreamsPresenter presenter();

    public void inject(StreamsFragment fragment);
}
