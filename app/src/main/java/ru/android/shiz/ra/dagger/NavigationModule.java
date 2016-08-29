package ru.android.shiz.ra.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.android.shiz.ra.IntentStarter;

/**
 * Created by kassava on 22.08.2016.
 */
@Module
public class NavigationModule {

    @Singleton @Provides
    public IntentStarter providesIntentStarter() {
        return new IntentStarter();
    }
}
