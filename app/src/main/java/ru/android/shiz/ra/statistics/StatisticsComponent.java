package ru.android.shiz.ra.statistics;

import javax.inject.Singleton;

import dagger.Component;
import ru.android.shiz.ra.dagger.StreamAppComponent;
import ru.android.shiz.ra.dagger.StreamModule;

/**
 * Created by kassava on 25.08.2016.
 */
@Singleton
@Component(
        modules = StreamModule.class,
        dependencies = StreamAppComponent.class)
public interface StatisticsComponent {

    public StatisticsPresenter presenter();
}
