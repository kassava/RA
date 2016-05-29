package ru.android.shiz.ra.flow;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import flow.KeyParceler;

/**
 * Class is responsible to write and read a “key” as parcelable
 *
 * Created by kassava on 24.04.2016.
 */
public final class RAAppKeyParceler implements KeyParceler {

    @NonNull @Override
    public Parcelable toParcelable(@NonNull Object key) {
        return (Parcelable) key;
    }

    @NonNull @Override
    public Object toKey(@NonNull Parcelable parcelable) {
        return parcelable;
    }
}
