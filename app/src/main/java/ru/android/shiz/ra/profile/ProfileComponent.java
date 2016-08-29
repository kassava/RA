package ru.android.shiz.ra.profile;

import javax.inject.Singleton;

import dagger.Component;
import ru.android.shiz.ra.dagger.ContactsModule;
import ru.android.shiz.ra.dagger.StreamAppComponent;
import ru.android.shiz.ra.dagger.StreamModule;

/**
 * Created by kassava on 24.08.2016.
 */
@Singleton
@Component(
        modules = {
                StreamModule.class, ContactsModule.class,

        },
        dependencies = StreamAppComponent.class)
public interface ProfileComponent {

    public ProfilePresenter presenter();
}
