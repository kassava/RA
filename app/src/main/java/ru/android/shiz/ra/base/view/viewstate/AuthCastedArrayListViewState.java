package ru.android.shiz.ra.base.view.viewstate;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;

import java.util.List;

import ru.android.shiz.ra.base.view.AuthView;

/**
 * Created by kassava on 21.08.2016.
 */
@SuppressWarnings("ParcelCreator")
public class AuthCastedArrayListViewState<D extends List<? extends Parcelable>, V extends AuthView<D>>
        extends CastedArrayListLceViewState<D, V> implements AuthViewState<D, V> {

    public AuthCastedArrayListViewState(){
    }

    protected AuthCastedArrayListViewState(Parcel source) {
        super(source);
    }

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
