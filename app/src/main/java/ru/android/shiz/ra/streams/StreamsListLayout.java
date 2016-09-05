package ru.android.shiz.ra.streams;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
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
import ru.android.shiz.ra.RAApp;
import ru.android.shiz.ra.model.Stream;
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
    private boolean isRetainInstance;

    private StreamsAdapter adapter;

    public StreamsListLayout(Context ctx, AttributeSet attributeSet)  {
        super(ctx, attributeSet);

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
                        Flow.get(getContext()).set(new StreamDetailsScreen(v.getId()));
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
        Log.d(LOG_TAG, "createPresenter: " + RAApp.getComponent().streamsPresenter());

        return RAApp.getComponent().streamsPresenter();
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

    @SuppressLint("MissingSuperCall")
    @Override
    public Parcelable onSaveInstanceState() {
//        Parcelable state = super.onSaveInstanceState();
//        Log.d(LOG_TAG, "onSaveInstanceState:" + super.onSaveInstanceState());
//        state = getViewState();
//        Log.d(LOG_TAG, "state: " + state);
//        return state;

        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, castedViewState().getCurrentViewState());
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
//        Log.d(LOG_TAG, "onRestoreInstanceState: " + state);
//        viewState = (CustomRestorableParcelableViewState) state;
//        super.onRestoreInstanceState(viewState);
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        Log.d(LOG_TAG, "SuperState:" + savedState.getState());


    }

    /**
     * Convenience class to save / restore the lock combination picker state. Looks clumsy
     * but once created is easy to maintain and use.
     */
    protected static class SavedState extends BaseSavedState {

        private final int state;

        private SavedState(Parcelable superState, int state) {
            super(superState);
            this.state = state;
        }

        private SavedState(Parcel in) {
            super(in);
            state = in.readInt();
        }

        public int getState() {
            return state;
        }

        @Override
        public void writeToParcel(Parcel destination, int flags) {
            super.writeToParcel(destination, flags);
            destination.writeInt(state);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }


    @Override
    public void onViewStateInstanceRestored(boolean instanceStateRetained) {
        // can be overridden in subclass
        Log.d(LOG_TAG, "onViewStateInstanceRestored: " + instanceStateRetained);
    }


    @Override
    public void onNewViewStateInstance() {
        Log.d(LOG_TAG, "onNewViewStateInstance");
        Log.d(LOG_TAG, "getViewState(): " + getViewState());

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
}
