package ru.android.shiz.ra.model.stream.service;

import javax.inject.Singleton;

import dagger.Component;
import ru.android.shiz.ra.dagger.NavigationModule;
import ru.android.shiz.ra.dagger.StreamAppComponent;
import ru.android.shiz.ra.dagger.StreamModule;

/**
 * Created by kassava on 24.08.2016.
 */
@Singleton
@Component(
        modules = { StreamModule.class, NavigationModule.class },
        dependencies = StreamAppComponent.class)
public interface ServiceComponent {

    public void inject(SendStreamService service);

    public void inject(GcmFakeIntentService service);
}
