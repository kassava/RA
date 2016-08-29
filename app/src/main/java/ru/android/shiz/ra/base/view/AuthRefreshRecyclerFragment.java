package ru.android.shiz.ra.base.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import java.util.List;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.base.view.viewstate.AuthCastedArrayListViewState;
import ru.android.shiz.ra.base.view.viewstate.AuthViewState;

/**
 * Created by kassava on 21.08.2016.
 */
public abstract class AuthRefreshRecyclerFragment<M extends List<? extends Parcelable>, V extends AuthView<M>, P extends MvpPresenter<V>>
        extends AuthRefreshFragment<M, V, P> {

    protected View emptyView;
    protected RecyclerView recyclerView;
    protected ListAdapter<M> adapter;

    protected abstract ListAdapter<M> createAdapter();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyView = view.findViewById(R.id.emptyView);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adapter = createAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setData(M data) {
        adapter.setItems(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public AuthViewState<M, V> createViewState() {
        return new AuthCastedArrayListViewState<>();
    }

    @Override
    public M getData() {
        return adapter.getItems();
    }

    @Override public void showContent() {
        if (adapter.getItemCount() == 0) {
            if (isRestoringViewState()) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                ObjectAnimator anim = ObjectAnimator.ofFloat(emptyView, "alpha", 0f, 1f).setDuration(300);
                anim.setStartDelay(250);
                anim.addListener(new AnimatorListenerAdapter() {

                    @Override public void onAnimationStart(Animator animation) {
                        emptyView.setVisibility(View.VISIBLE);
                    }
                });
                anim.start();
            }
        } else {
            emptyView.setVisibility(View.GONE);
        }

        super.showContent();
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        if (!pullToRefresh) {
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        if (!pullToRefresh) {
            emptyView.setVisibility(View.GONE);
        }
    }
}
