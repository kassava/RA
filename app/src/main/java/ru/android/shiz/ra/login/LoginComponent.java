package ru.android.shiz.ra.login;

import javax.inject.Singleton;

import dagger.Component;
import ru.android.shiz.ra.dagger.StreamAppComponent;
import ru.android.shiz.ra.dagger.StreamModule;

/**
 * Created by kassava on 22.08.2016.
 */
@Singleton
@Component(modules = StreamModule.class,
        dependencies = StreamAppComponent.class)
public interface LoginComponent {

    public LoginPresenter presenter();
}
