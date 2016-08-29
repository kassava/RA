package ru.android.shiz.ra.base.view;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

/**
 * Created by kassava on 21.08.2016.
 */
public interface AuthView<M> extends MvpLceView<M> {

    public void showAuthenticationRequired();
}
