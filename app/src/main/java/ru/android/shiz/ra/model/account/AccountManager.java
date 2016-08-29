package ru.android.shiz.ra.model.account;

import rx.Observable;

/**
 * Created by kassava on 24.08.2016.
 */
public interface AccountManager {
    Observable<Account> doLogin(AuthCredentials credentials);

    Account getCurrentAccount();

    boolean isUserAuthenticated();
}