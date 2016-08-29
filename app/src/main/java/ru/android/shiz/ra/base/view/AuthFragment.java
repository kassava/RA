package ru.android.shiz.ra.base.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import javax.inject.Inject;

import ru.android.shiz.ra.IntentStarter;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.base.view.viewstate.AuthViewState;

/**
 * Base fragment that handles displaying Authentication state and LCE. Also includes Butterknife,
 * FragmentArgs and Icepick
 *
 * Created by kassava on 21.08.2016.
 */
public abstract class AuthFragment<AV extends View, M, V extends AuthView<M>, P extends MvpPresenter<V>>
        extends BaseLceFragment<AV, M, V, P> implements AuthView<M> {

    protected View authView;
    @Inject IntentStarter intentStarter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authView = view.findViewById(R.id.authView);
        authView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onAuthViewClicked();
            }
        });
    }

    protected void onAuthViewClicked() {
        intentStarter.showAuthentication(getActivity());
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        if (pullToRefresh) {
            return "An error has occurred!";
        } else {
            return "An error has occurred. Click here to retry";
        }
    }

    @Override
    public void showAuthenticationRequired() {
        AuthViewState vs = (AuthViewState) viewState;
        vs.setShowingAuthenticationRequired();

        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
        authView.setVisibility(View.VISIBLE);
    }

    @Override public void showContent() {
        super.showContent();
        authView.setVisibility(View.GONE);
    }

    @Override public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        authView.setVisibility(View.GONE);
    }

    @Override public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        authView.setVisibility(View.GONE);
    }

    @Override
    public abstract AuthViewState<M, V> createViewState();
}
