package ru.android.shiz.ra.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by kassava on 22.08.2016.
 */
public interface LoginView extends MvpView {

    public void showLoginForm();

    public void showError();

    public void showLoading();

    public void loginSuccessful();
}