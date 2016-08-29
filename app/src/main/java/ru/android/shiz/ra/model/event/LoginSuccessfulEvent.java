package ru.android.shiz.ra.model.event;

import ru.android.shiz.ra.model.account.Account;

/**
 * Fired to inform that the login was successful
 *
 * Created by kassava on 24.08.2016.
 */
public class LoginSuccessfulEvent {

    private Account account;

    public LoginSuccessfulEvent(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}
