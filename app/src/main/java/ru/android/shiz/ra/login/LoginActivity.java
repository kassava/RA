package ru.android.shiz.ra.login;

import android.os.Bundle;

import ru.android.shiz.ra.R;
import ru.android.shiz.ra.base.view.BaseActivity;

/**
 * Created by kassava on 22.08.2016.
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new LoginFragment())
                    .commit();
        }
    }
}
