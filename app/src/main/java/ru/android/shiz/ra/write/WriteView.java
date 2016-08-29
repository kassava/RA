package ru.android.shiz.ra.write;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by kassava on 25.08.2016.
 */
public interface WriteView extends MvpView {

    public void showForm();

    public void showLoading();

    public void showError(Throwable e);

    public void showAuthenticationRequired();

    public void finishBecauseSuccessful();
}
