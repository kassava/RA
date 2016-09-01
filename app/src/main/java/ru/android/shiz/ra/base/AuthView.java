package ru.android.shiz.ra.base;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

/**
 * Created by kassava on 01.09.2016.
 */
public interface AuthView<M> extends MvpLceView<M>{

    public void showAuthenticationRequired();
}
