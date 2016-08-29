package ru.android.shiz.ra.base.view.viewstate;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import ru.android.shiz.ra.base.view.AuthView;

/**
 * Created by kassava on 21.08.2016.
 */
public interface AuthViewState<D, V extends AuthView<D>> extends LceViewState<D, V> {

    public static final int SHOWING_AUTHENTICATION_REQUIRED = 2;

    public void setShowingAuthenticationRequired();
}
