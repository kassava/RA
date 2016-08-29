package ru.android.shiz.ra.login;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hkm.ui.processbutton.iml.ActionProcessButton;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.RaApp;
import ru.android.shiz.ra.base.view.BaseViewStateFragment;
import ru.android.shiz.ra.model.account.AuthCredentials;
import ru.android.shiz.ra.utils.KeyboardUtils;

/**
 * Created by kassava on 22.08.2016.
 */
public class LoginFragment extends BaseViewStateFragment<LoginView, LoginPresenter>
        implements LoginView {

    private final String LOG_TAG = LoginFragment.class.getSimpleName();

    @BindView(R.id.username) EditText username;
    @BindView(R.id.password) EditText password;
    @BindView(R.id.loginButton) ActionProcessButton loginButton;
    @BindView(R.id.errorView) TextView errorView;
    @BindView(R.id.loginForm) ViewGroup loginForm;

    private LoginComponent loginComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_login;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton.setMode(ActionProcessButton.Mode.ENDLESS);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "onClick");
            }
        });

        loginButton.setOnClickNormalState(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "onCliclNormalState");
                onLoginClicked();
            }
        }).build();

        Log.d(LOG_TAG, "LoginFragment.onViewCreated: " + loginButton.getId());

        int startDelay = getResources().getInteger(android.R.integer.config_mediumAnimTime) + 100;
        LayoutTransition transition = new LayoutTransition();
        transition.enableTransitionType(LayoutTransition.CHANGING);
        transition.setStartDelay(LayoutTransition.APPEARING, startDelay);
        transition.setStartDelay(LayoutTransition.CHANGE_APPEARING, startDelay);
        loginForm.setLayoutTransition(transition);
    }

    @Override
    public ViewState createViewState() {
        return new LoginViewState();
    }

    @Override
    public LoginPresenter createPresenter() {
        return loginComponent.presenter();
    }

    @OnClick(R.id.loginButton)
    public void onLoginClicked() {

        Log.d(LOG_TAG, "LoginFragment.onLoginClicked");

        // Check for empty fields
        String uname = username.getText().toString();
        String pass = password.getText().toString();

        loginForm.clearAnimation();

        if (TextUtils.isEmpty(uname)) {
            username.clearAnimation();
            Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
            username.startAnimation(shake);
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            password.clearAnimation();
            Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
            password.startAnimation(shake);
            return;
        }

        // Hide keyboard
        if (!KeyboardUtils.hideKeyboard(username)) {
            KeyboardUtils.hideKeyboard(password);
        }

        // Start login
        presenter.doLogin(new AuthCredentials(uname, pass));
    }

    @Override public void onNewViewStateInstance() {
        showLoginForm();
    }

    @Override public void showLoginForm() {

        LoginViewState vs = (LoginViewState) viewState;
        vs.setShowLoginForm();

        errorView.setVisibility(View.GONE);
        setFormEnabled(true);
        loginButton.setProgress(0);
    }

    @Override public void showError() {

        LoginViewState vs = (LoginViewState) viewState;
        vs.setShowError();

        setFormEnabled(true);
        loginButton.setProgress(0);

        if (!isRestoringViewState()) {
            // Enable animations only if not restoring view state
            loginForm.clearAnimation();
            Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
            loginForm.startAnimation(shake);
        }

        errorView.setVisibility(View.VISIBLE);
    }

    @Override public void showLoading() {

        LoginViewState vs = (LoginViewState) viewState;
        vs.setShowLoading();
        errorView.setVisibility(View.GONE);
        setFormEnabled(false);
        // any progress between 0 - 100 shows animation
        loginButton.setProgress(30);
    }

    private void setFormEnabled(boolean enabled) {
        username.setEnabled(enabled);
        password.setEnabled(enabled);
        loginButton.setEnabled(enabled);
    }

    @Override public void loginSuccessful() {
        loginButton.setProgress(100); // We are done
        getActivity().finish();
        getActivity().overridePendingTransition(0, R.anim.zoom_out);
    }

    @Override protected void injectDependencies() {
        loginComponent = DaggerLoginComponent.builder()
                .streamAppComponent(RaApp.getStreamComponents())
                .build();
    }
}