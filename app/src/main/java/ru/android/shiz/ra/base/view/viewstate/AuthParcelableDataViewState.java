package ru.android.shiz.ra.base.view.viewstate;

import android.os.Parcelable;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.ParcelableDataLceViewState;

import ru.android.shiz.ra.base.view.AuthView;

/**
 * Created by kassava on 21.08.2016.
 */
@SuppressWarnings("ParcelCreator")
public class AuthParcelableDataViewState<D extends Parcelable, V extends AuthView<D>>
        extends ParcelableDataLceViewState<D, V> implements AuthViewState<D, V> {

    @Override public void apply(V view, boolean retained) {

        if (currentViewState == SHOWING_AUTHENTICATION_REQUIRED) {
            view.showAuthenticationRequired();
        } else {
            super.apply(view, retained);
        }
    }

    @Override public void setShowingAuthenticationRequired() {
        currentViewState = SHOWING_AUTHENTICATION_REQUIRED;

        // Delete any previous stored data
        loadedData = null;
        exception = null;
    }
}
