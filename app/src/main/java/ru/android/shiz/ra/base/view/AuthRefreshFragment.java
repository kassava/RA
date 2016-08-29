package ru.android.shiz.ra.base.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import ru.android.shiz.ra.R;

/**
 * Created by kassava on 21.08.2016.
 */
public abstract class AuthRefreshFragment<M, V extends AuthView<M>, P extends MvpPresenter<V>>
        extends AuthFragment<SwipeRefreshLayout, M, V, P>
        implements AuthView<M>, SwipeRefreshLayout.OnRefreshListener {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contentView.setOnRefreshListener(this);
        int [] colors = getActivity().getResources().getIntArray(R.array.loading_colors);
        contentView.setColorSchemeColors(colors);
    }

    @Override
    public void showAuthenticationRequired() {
        super.showAuthenticationRequired();
        contentView.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void showContent() {
        super.showContent();
        contentView.setRefreshing(false);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        contentView.setRefreshing(false);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        if (pullToRefresh && !contentView.isRefreshing()) {
            // Workaround for measure bug: https://code.google.com/p/android/issues/detail?id=77712
            contentView.post(new Runnable() {
                @Override public void run() {
                    contentView.setRefreshing(true);
                }
            });
        }
    }
}
