package ru.android.shiz.ra.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.android.shiz.ra.streams.StreamsPresenter;

/**
 * Created by kassava on 09.05.2016.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    public StreamsPresenter streamsPresenter();

//    public StreamDetailsPresenter streamDetailsPresenter();
}
