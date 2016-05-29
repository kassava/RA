package ru.android.shiz.ra.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.android.shiz.ra.model.RAApi;

/**
 * Created by kassava on 09.05.2016.
 */
@Module
public class ApplicationModule {

    @Provides @Singleton
    public RAApi provideRA() {
        return new RAApi();
    }
}
