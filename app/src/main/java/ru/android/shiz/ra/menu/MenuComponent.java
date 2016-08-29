package ru.android.shiz.ra.menu;

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
        dependencies = StreamAppComponent.class

) public interface MenuComponent {

    MenuPresenter presenter();

    void inject(MenuFragment menuFragment);
}