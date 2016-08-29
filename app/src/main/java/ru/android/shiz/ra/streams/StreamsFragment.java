package ru.android.shiz.ra.streams;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.melnykov.fab.FloatingActionButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.shiz.ra.IntentStarter;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.RaApp;
import ru.android.shiz.ra.base.view.BaseStreamsFragment;
import ru.android.shiz.ra.dagger.NavigationModule;
import ru.android.shiz.ra.model.stream.Label;
import ru.android.shiz.ra.model.stream.Stream;

/**
 * Created by kassava on 22.08.2016.
 */
public class StreamsFragment extends BaseStreamsFragment<StreamsView, StreamsPresenter>
        implements StreamsView, StreamsAdapter.StreamClickedListener, StreamsAdapter.StreamStarListener {

    @Arg
    Label label;

    @Inject
    IntentStarter intentStarter;

    @BindView(R.id.createStream)
    FloatingActionButton createStreamButton;

    StreamsComponent streamsComponent;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_streams;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createStreamButton.attachToRecyclerView(recyclerView);
    }

    @Override
    public StreamsPresenter createPresenter() {
        return streamsComponent.presenter();
    }

    @Override
    public void loadData(boolean b) {
        presenter.load(b, label);
    }

    @Override
    protected void injectDependencies() {
        streamsComponent = DaggerStreamsComponent.builder()
                .streamAppComponent(RaApp.getStreamComponents())
                .navigationModule(new NavigationModule())
                .build();
        streamsComponent.inject(this);
    }

    @Override
    public void changeLabel(Stream stream, String labelName) {

        StreamsAdapter.StreamInAdapterResult result = ((StreamsAdapter) adapter).findStream(stream);
        if (result.isFound() && !labelName.equals(this.label.getName())) {
            // Found in adapter, but label has changed --> remove it
            adapter.getItems().remove(result.getIndex());
            adapter.notifyItemRemoved(result.getIndex());
            if (adapter.getItemCount() == 0) {
                emptyView.setVisibility(View.VISIBLE);
            }
        } else if (!result.isFound() && labelName.equals(this.label.getName())) {
            // Not found, but should be added
            adapter.getItems().add(result.getIndex(), stream);
            adapter.notifyItemInserted(result.getIndex());
            if (result.getIndex() == 0) {
                recyclerView.scrollToPosition(0);
            }

            if (adapter.getItemCount() > 0) {
                emptyView.setVisibility(View.GONE);
            }
        }
    }

    @Override public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        if (!pullToRefresh) {
            createStreamButton.setVisibility(View.GONE);
        }
    }

    @Override public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        if (!pullToRefresh) {
            createStreamButton.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.createStream) public void onCreateStreamClicked() {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), createStreamButton,
                        getString(R.string.shared_write_action));

        intentStarter.showWriteStream(getActivity(), null, options.toBundle());
    }

    @Override public void showContent() {
        super.showContent();
        if (createStreamButton.getVisibility() != View.VISIBLE) {
            createStreamButton.setVisibility(View.VISIBLE);

            if (!isRestoringViewState()) {
                PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("scaleX", 0, 1);
                PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("scaleY", 0, 1);
                ObjectAnimator animator =
                        ObjectAnimator.ofPropertyValuesHolder(createStreamButton, holderX, holderY);
                animator.setInterpolator(new OvershootInterpolator());
                animator.start();
            }
        }
    }
}
