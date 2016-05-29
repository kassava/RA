package ru.android.shiz.ra;

import android.app.Application;

import ru.android.shiz.ra.dagger.ApplicationComponent;

/**
 * Created by kassava on 10.05.2016.
 */
public class RAApp extends Application {

    private static ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = ru.android.shiz.ra.dagger.DaggerApplicationComponent.create();
    }

    public static ApplicationComponent getComponent() {
        return component;
    }
}
