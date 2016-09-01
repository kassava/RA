package ru.android.shiz.ra.base.viewstate;

import android.annotation.SuppressLint;
import android.os.Parcelable;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.ParcelableDataLceViewState;

import ru.android.shiz.ra.base.AuthView;

/**
 * Created by kassava on 01.09.2016.
 */
@SuppressLint("ParcelCreator")
public class AuthParcelableDataViewState<D extends Parcelable, V extends AuthView<D>>
        extends ParcelableDataLceViewState<D, V> implements AuthViewState<D, V> {

    @Override
    public void apply(V view, boolean retained) {
        if (currentViewState == SHOWING_AUTHENTICATION_REQUIRED) {
            view.showAuthenticationRequired();
        } else {
            super.apply(view, retained);
        }
    }

    @Override
    public void setShowingAuthenticationRequired() {
        currentViewState = SHOWING_AUTHENTICATION_REQUIRED;

        // Delete any previous stored data
        loadedData = null;
        exception = null;
    }
}
