package ru.android.shiz.ra.broadcastdetails;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.layout.MvpViewStateFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import flow.Flow;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.RaApp;
import ru.android.shiz.ra.model.InfoText;
import ru.android.shiz.ra.model.StreamDetail;
import ru.android.shiz.ra.mortar.DetailsMortarPresenter;
import ru.android.shiz.ra.base.viewstate.CustomRestorableParcelableViewState;
import ru.android.shiz.ra.broadcasts.BroadcastsListLayout;

/**
 * Created by kassava on 07.09.16.
 */
public class BroadcastDetailsLayout extends MvpViewStateFrameLayout<BroadcastDetailsView, BroadcastDetailsPresenter>
        implements BroadcastDetailsView {

    private final String LOG_TAG = BroadcastsListLayout.class.getSimpleName();

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.contentView) View contentView;
    @BindView(R.id.errorView) View errorView;
    @BindView(R.id.loadingView) View loadingView;
    @BindView(R.id.highlightImage) ImageView hilightImage;
    @BindView(R.id.collapsingToolbar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private int streamId;

    private DetailsMortarPresenter mortarPresenter;

    private Context context;

    private StreamDetail detailData = null;
    private InfoAdapter adapter;

    public BroadcastDetailsLayout(Context ctx, AttributeSet attributeSet) {
        super(ctx, attributeSet);
        this.context = ctx;
        adapter = new InfoAdapter(LayoutInflater.from(context));
        mortarPresenter = (DetailsMortarPresenter) ctx.getSystemService(DetailsMortarPresenter.class.getName());
    }

    @Override
    protected void onAttachedToWindow() {
        boolean isRetainInstance;
        isRetainInstance = mortarPresenter.getViewState() != null;
        if (isRetainInstance) {
            viewState = mortarPresenter.getViewState();
        }
        BroadcastDetailsScreen broadcastDetailsScreen = Flow.getKey(this);
        streamId = broadcastDetailsScreen != null ? broadcastDetailsScreen.getStreamId() : 0;
        super.onAttachedToWindow();
        mortarPresenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d(LOG_TAG, "onDetachedFromWindow");
        mortarPresenter.setViewState((CustomRestorableParcelableViewState) getViewState());
        mortarPresenter.dropView(this);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Flow.get(context).goBack();
                castedViewState().setStateShowLoading(false);
            }
        });
        hilightImage.setColorFilter(new PorterDuffColorFilter(
                R.color.image_highlight_darking, PorterDuff.Mode.SRC_ATOP));
        errorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(false);
            }
        });
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 3);
        ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.items.get(position) instanceof InfoText) {
                    return 3;
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);
    }

    @NonNull
    @Override
    public BroadcastDetailsPresenter createPresenter() {
        return RaApp.getComponent().broadcastDetailsPresenter();
    }


    @NonNull
    @Override
    public ViewState<BroadcastDetailsView> createViewState() {
        return new CustomRestorableParcelableViewState<StreamDetail, BroadcastDetailsView>();
    }

    @Override
    public void onNewViewStateInstance() {
        loadData(false);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        castedViewState().setStateShowLoading(pullToRefresh);

        loadingView.setVisibility(VISIBLE);
        errorView.setVisibility(GONE);
        contentView.setVisibility(GONE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void showContent() {
        castedViewState().setStateShowContent(detailData);

        if (isRestoringViewState()) {
            Log.d(LOG_TAG, "showContent isRestoringViewState");

            contentView.setVisibility(VISIBLE);
            errorView.setVisibility(GONE);
            loadingView.setVisibility(GONE);
        } else {
            Log.d(LOG_TAG, "showContent isRestoringViewState else");
            contentView.setAlpha(0f);
            contentView.setVisibility(VISIBLE);
            contentView.animate().alpha(1f).start();
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
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        castedViewState().setStateShowError(e, pullToRefresh);

        if (pullToRefresh) {
            contentView.setVisibility(VISIBLE);
            errorView.setVisibility(GONE);
            loadingView.setVisibility(GONE);
            Toast.makeText(context, "An error has occurred", Toast.LENGTH_SHORT).show();
        } else {
            contentView.setVisibility(GONE);
            loadingView.setVisibility(GONE);
            errorView.setVisibility(VISIBLE);
        }
    }

    @Override
    public void setData(StreamDetail data) {
        this.detailData = data;
        collapsingToolbar.setTitle(String.valueOf(data.getId()));
        toolbar.setTitle(String.valueOf(data.getId()));
        adapter.items = data.getInfos();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadDetails(streamId);
    }

    private CustomRestorableParcelableViewState<StreamDetail, BroadcastDetailsView> castedViewState() {
        return (CustomRestorableParcelableViewState<StreamDetail, BroadcastDetailsView>)viewState;
    }
}
