package ru.android.shiz.ra.model.account;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kassava on 24.08.2016.
 */
public class DefaultAccountManager implements AccountManager {

    private Account currentAccount;

    /**
     * Returns the Account observable
     */
    @Override public Observable<Account> doLogin(AuthCredentials credentials) {

        return Observable.just(credentials).flatMap(new Func1<AuthCredentials, Observable<Account>>() {
            @Override public Observable<Account> call(AuthCredentials credentials) {

                try {
                    // Simulate network delay
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (credentials.getUsername().equals("ted") && credentials.getPassword().equals("robin")) {
                    currentAccount = new Account();
                    return Observable.just(currentAccount);
                }

                return Observable.error(new LoginException());
            }
        });
    }

    @Override public Account getCurrentAccount() {
        return currentAccount;
    }

    @Override public boolean isUserAuthenticated() {
        return currentAccount != null;
    }
}
