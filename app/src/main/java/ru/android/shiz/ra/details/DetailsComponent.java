package ru.android.shiz.ra.details;

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
        modules = { StreamModule.class, NavigationModule.class },
        dependencies = StreamAppComponent.class
)
public interface DetailsComponent {

    public DetailsPresenter presenter();

    public void inject(DetailsFragment fragment);

}