package ru.android.shiz.ra.model.account;

/**
 * Created by kassava on 24.08.2016.
 */
public class AuthCredentials {

    String username;
    String password;

    public AuthCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

}
