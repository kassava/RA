package ru.android.shiz.ra.streams;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.layout.MvpViewStateFrameLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import flow.Flow;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.RaApp;
import ru.android.shiz.ra.model.Stream;
import ru.android.shiz.ra.mortar.MortarPresenter;
import ru.android.shiz.ra.streamdetails.StreamDetailsScreen;

/**
 * Created by kassava on 28.04.2016.
 */
public class StreamsListLayout extends MvpViewStateFrameLayout<StreamsView, StreamsPresenter> implements StreamsView,
        SwipeRefreshLayout.OnRefreshListener {

    private final String LOG_TAG = this.getClass().getSimpleName();

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.errorView) View errorView;
    @BindView(R.id.loadingView) View loadingView;

    private Context context;
    private boolean isRetainInstance = false;

    private StreamsAdapter adapter;

    private final MortarPresenter mortarPresenter;

    public StreamsListLayout(Context ctx, AttributeSet attributeSet)  {
        super(ctx, attributeSet);

        // presenter for mortar
        mortarPresenter = (MortarPresenter) ctx.getSystemService(MortarPresenter.class.getName());

        Log.d(LOG_TAG, "StreamsListLayout");

        this.context = ctx;
        this.isRetainInstance = true;

        LayoutInflater.from(context).inflate(R.layout.recycler_swiperefresh_view, this, true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        errorView = findViewById(R.id.errorView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        loadingView = findViewById(R.id.loadingView);

        adapter = new StreamsAdapter(LayoutInflater.from(context),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(LOG_TAG, "onClick: " + recyclerView.getChildLayoutPosition(v));

//                        Flow.get(getContext()).set(new StreamDetailsScreen(v.getId()));
                        Flow.get(getContext()).set(new StreamDetailsScreen(recyclerView.getChildLayoutPosition(v)));
                    }
                }
        );

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(false);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @NonNull
    @Override
    public StreamsPresenter createPresenter() {
        Log.d(LOG_TAG, "createPresenter: " + RaApp.getComponent().streamsPresenter());

        return RaApp.getComponent().streamsPresenter();
    }

    @NonNull
    @Override
    public ViewState<StreamsView> createViewState() {
        Log.d(LOG_TAG, "createViewState: " + getViewState());

        if (isRestoringViewState()) {
            Log.d(LOG_TAG, "isRestoringViewState");
        }
        return new CustomRestorableParcelableViewState<List<Stream>, StreamsView>();
    }

    @Override
    public void onViewStateInstanceRestored(boolean instanceStateRetained) {
        // can be overridden in subclass
        Log.d(LOG_TAG, "onViewStateInstanceRestored: " + instanceStateRetained);
    }


    @Override
    public void onNewViewStateInstance() {
        Log.d(LOG_TAG, "onNewViewStateInstance"  + getViewState());

        loadData(false);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        Log.d(LOG_TAG, "showLoading");

        castedViewState().setStateShowLoading(pullToRefresh);

        if (pullToRefresh) {
            loadingView.setVisibility(GONE);
            errorView.setVisibility(GONE);
            swipeRefreshLayout.setVisibility(VISIBLE);
            swipeRefreshLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            swipeRefreshLayout.setRefreshing(true);
                                        }
                                    });
        } else {
            loadingView.setVisibility(VISIBLE);
            errorView.setVisibility(GONE);
            swipeRefreshLayout.setVisibility(GONE);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void showContent() {
        Log.d(LOG_TAG, "showContent");

        castedViewState().setStateShowContent(adapter.getItems());


        if (isRestoringViewState()) {
            Log.d(LOG_TAG, "showContent isRestoringViewState");

            swipeRefreshLayout.setVisibility(VISIBLE);
            errorView.setVisibility(GONE);
            loadingView.setVisibility(GONE);
        } else {
            Log.d(LOG_TAG, "showContent isRestoringViewState else");
            swipeRefreshLayout.setAlpha(0f);
            swipeRefreshLayout.setVisibility(VISIBLE);
            swipeRefreshLayout.animate().alpha(1f).start();
            loadingView.animate().alpha(0f).
                    withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            loadingView.setVisibility(GONE);
                            loadingView.setAlpha(1f);
                        }
                    }

            );

            errorView.setVisibility(GONE);
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        Log.d(LOG_TAG, "showError");

        castedViewState().setStateShowError(e, pullToRefresh);

        if (pullToRefresh) {
            swipeRefreshLayout.setVisibility(VISIBLE);
            errorView.setVisibility(GONE);
            loadingView.setVisibility(GONE);
            Toast.makeText(context, "An error has occurred", Toast.LENGTH_SHORT).show();
        } else {
            swipeRefreshLayout.setVisibility(GONE);
            loadingView.setVisibility(GONE);
            errorView.setVisibility(VISIBLE);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setData(List<Stream> data) {
        Log.d(LOG_TAG, "setData");

        adapter.setItems(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        Log.d(LOG_TAG, "loadData");

        presenter.loadStreams(pullToRefresh);
    }

    @Override
    public void onRefresh() {
        Log.d(LOG_TAG, "onRefresh");

        loadData(true);
    }

    private CustomRestorableParcelableViewState<List<Stream>, StreamsView> castedViewState() {
        return (CustomRestorableParcelableViewState<List<Stream>, StreamsView>)viewState;
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d(LOG_TAG, "onDetachedFromWindow");
        mortarPresenter.setViewState((CustomRestorableParcelableViewState) getViewState());
        mortarPresenter.dropView(this);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d(LOG_TAG, "onAttachedFromWindow: " + viewState);
        if (mortarPresenter.getViewState() != null) {
            isRetainInstance = true;
        } else {
            isRetainInstance = false;
        }
        if (isRetainInstance) {
            viewState = mortarPresenter.getViewState();
        }
        super.onAttachedToWindow();
        mortarPresenter.takeView(this);
    }
}
