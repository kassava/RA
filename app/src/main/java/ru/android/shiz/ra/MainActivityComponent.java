package ru.android.shiz.ra;

import javax.inject.Singleton;

import dagger.Component;
import ru.android.shiz.ra.dagger.NavigationModule;

/**
 * Created by kassava on 25.08.2016.
 */
@Singleton
@Component(
        modules = NavigationModule.class) public interface MainActivityComponent {

    public void inject(MainActivity activity);
}
