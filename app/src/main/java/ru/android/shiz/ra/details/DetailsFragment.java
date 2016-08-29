package ru.android.shiz.ra.details;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.shiz.ra.IntentStarter;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.RaApp;
import ru.android.shiz.ra.base.view.AuthFragment;
import ru.android.shiz.ra.base.view.viewstate.AuthParcelableDataViewState;
import ru.android.shiz.ra.base.view.viewstate.AuthViewState;
import ru.android.shiz.ra.label.LabelLayout;
import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.ui.transition.ExcludedExplodeTransition;
import ru.android.shiz.ra.ui.transition.ExplodeFadeEnterTransition;
import ru.android.shiz.ra.ui.transition.TextSizeEnterSharedElementCallback;
import ru.android.shiz.ra.ui.transition.TextSizeTransition;
import ru.android.shiz.ra.ui.view.StarView;

/**
 * Created by kassava on 22.08.2016.
 */
public class DetailsFragment extends AuthFragment<TextView, Stream, DetailsView, DetailsPresenter>
        implements DetailsView, View.OnClickListener {

    @Arg
    int streamId;
    @Arg String subject;
    @Arg int senderProfilePic;
    @Arg String senderName;
    @Arg String senderEmail;
    @Arg long date;
    @Arg boolean starred;

    @Inject
    IntentStarter intentStarter;

    @BindView(R.id.senderPic) ImageView senderImageView;
    @BindView(R.id.subject) TextView subjectView;
    @BindView(R.id.date) TextView dateView;
    @BindView(R.id.starButton) StarView starView;
    @BindView(R.id.replay) FloatingActionButton replayView;
    @BindView(R.id.senderName) TextView senderNameView;
    @BindView(R.id.senderMail) TextView senderStreamView;
    @BindView(R.id.separatorLine) View separatorLine;
    @BindView(R.id.label) LabelLayout labelView;
    @BindView(R.id.scrollView) ObservableScrollView scrollView;

    private DetailsComponent detailsComponent;

    Format format = new SimpleDateFormat("d. MMM",  Locale.getDefault());

    // The loaded data
    private Stream stream;

    @Override
    public AuthViewState<Stream, DetailsView> createViewState() {
        return new AuthParcelableDataViewState<>();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_stream_details;
    }

    @TargetApi(21) @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        replayView.attachToScrollView(scrollView);
        starView.setOnClickListener(this);

        subjectView.setText(subject);
        senderImageView.setImageResource(senderProfilePic);
        senderNameView.setText(senderName);
        senderStreamView.setText(senderEmail);
        starView.setStarred(starred);
        dateView.setText(format.format(new Date(date)));

        senderImageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (stream != null) {
                    intentStarter.showProfile(getActivity(), stream.getSender());
                }
            }
        });

        // Shared element animation
        if (Build.VERSION.SDK_INT >= 21 && !isTablet()) {

            initTransitions();

            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override public boolean onPreDraw() {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    getActivity().startPostponedEnterTransition();
                    return true;
                }
            });
        }
    }

    @TargetApi(21) private void initTransitions() {

        Window window = getActivity().getWindow();
        window.setEnterTransition(
                new ExplodeFadeEnterTransition(senderNameView, senderStreamView, separatorLine));
        window.setExitTransition(new ExcludedExplodeTransition());
        window.setReenterTransition(new ExcludedExplodeTransition());
        window.setReturnTransition(new ExcludedExplodeTransition());

        TransitionSet textSizeSet = new TransitionSet();
        textSizeSet.addTransition(
                TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.move));
        TextSizeTransition textSizeTransition = new TextSizeTransition();
        textSizeTransition.addTarget(R.id.subject);
        textSizeTransition.addTarget(getString(R.string.shared_stream_subject));

        textSizeSet.addTransition(textSizeTransition);
        textSizeSet.setOrdering(TransitionSet.ORDERING_TOGETHER);

        window.setSharedElementEnterTransition(textSizeSet);
        getActivity().setEnterSharedElementCallback(
                new TextSizeEnterSharedElementCallback(getActivity()));
    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.tablet);
    }

    @Override public Stream getData() {
        return stream;
    }

    @Override public DetailsPresenter createPresenter() {
        return detailsComponent.presenter();
    }

    @Override public void setData(Stream data) {
        this.stream = data;

        senderImageView.setImageResource(data.getSender().getImageRes());
        senderNameView.setText(data.getSender().getName());
        senderStreamView.setText(data.getSender().getEstream());
        subjectView.setText(data.getSubject());
        contentView.setText(data.getText() + data.getText() + data.getText() + data.getText());
        starView.setStarred(data.isStarred());
        dateView.setText(format.format(data.getDate()));
        labelView.setMail(data);
        labelView.setVisibility(View.VISIBLE);
        replayView.setVisibility(View.VISIBLE);

        // Animate only if not restoring
        if (!isRestoringViewState()) {
            labelView.setAlpha(0f);
            labelView.animate().alpha(1f).setDuration(150).start();

            PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("scaleX", 0, 1);
            PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("scaleY", 0, 1);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(replayView, holderX, holderY);
            animator.setInterpolator(new OvershootInterpolator());
            animator.start();
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadStream(streamId);
    }

    public int getStreamId() {
        return streamId;
    }

    @Override
    public void onClick(View v) {
        if (stream != null) {
            presenter.starStream(stream, !stream.isStarred());
        } else {
            Toast.makeText(getActivity(), R.string.error_wait_stream_loaded, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void markStreamAsStared(int streamId) {
        if (stream.getId() == streamId) {
            stream.setStarred(true);
            starView.setStarred(true);
        }
    }

    @Override
    public void markStreamAsUnstared(int streamId) {
        if (stream.getId() == streamId) {
            stream.setStarred(false);
            starView.setStarred(false);
        }
    }

    private void showStarErrorToast(int messageRes, Stream stream) {
        Toast.makeText(getActivity(), String.format(getString(messageRes), stream.getSender().getName()),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showStaringFailed(Stream stream) {
        showStarErrorToast(R.string.error_staring_stream, stream);
    }

    @Override
    public void showUnstaringFailed(Stream stream) {
        showStarErrorToast(R.string.error_unstaring_stream, stream);
    }

    @OnClick(R.id.replay) public void onReplayClicked() {

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), replayView,
                        getString(R.string.shared_write_action));

        intentStarter.showWriteStream(getActivity(), stream, options.toBundle());
    }

    @Override public void markStreamAsRead(Stream stream, boolean read) {
        // TODO: currently there is no UI component that shows if that stream has been read or not
    }

    @Override protected void injectDependencies() {
        detailsComponent =
                DaggerDetailsComponent.builder().streamAppComponent(RaApp.getStreamComponents()).build();
        detailsComponent.inject(this);
    }
}
