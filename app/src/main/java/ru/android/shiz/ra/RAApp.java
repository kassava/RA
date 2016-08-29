package ru.android.shiz.ra;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import ru.android.shiz.ra.dagger.StreamAppComponent;

/**
 * Created by kassava on 10.05.2016.
 */
public class RaApp extends Application {

    private RefWatcher refWatcher;

    private static StreamAppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = ru.android.shiz.ra.dagger.DaggerStreamAppComponent.create();
        refWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        RaApp raApp = (RaApp) context.getApplicationContext();
        return raApp.refWatcher;
    }

    public static StreamAppComponent getStreamComponents() {
        return component;
    }
}
