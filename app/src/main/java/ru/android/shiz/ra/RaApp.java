package ru.android.shiz.ra;

import android.app.Application;

import mortar.MortarScope;
import ru.android.shiz.ra.dagger.ApplicationComponent;

/**
 * Created by kassava on 10.05.2016.
 */
public class RaApp extends Application {

    private static ApplicationComponent component;
    private MortarScope rootScope;

    @Override
    public void onCreate() {
        super.onCreate();
        component = ru.android.shiz.ra.dagger.DaggerApplicationComponent.create();
    }

    @Override
    public Object getSystemService(String name) {
        if (rootScope == null) rootScope = MortarScope.buildRootScope().build("Root");

        return rootScope.hasService(name) ? rootScope.getService(name) : super.getSystemService(name);
    }

    public static ApplicationComponent getComponent() {
        return component;
    }
}
