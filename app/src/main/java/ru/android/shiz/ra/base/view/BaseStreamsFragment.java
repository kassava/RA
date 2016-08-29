package ru.android.shiz.ra.base.view;

import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import ru.android.shiz.ra.IntentStarter;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.base.presenter.BaseRxStreamPresenter;
import ru.android.shiz.ra.model.contact.Person;
import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.streams.StreamsAdapter;
import ru.android.shiz.ra.streams.StreamsAdapterHolders;

/**
 * Created by kassava on 21.08.2016.
 */
public abstract class BaseStreamsFragment<V extends BaseStreamView<List<Stream>>, P extends BaseRxStreamPresenter<V, List<Stream>>>
        extends AuthRefreshRecyclerFragment<List<Stream>, V, P>
        implements BaseStreamView<List<Stream>>, StreamsAdapter.StreamClickedListener,
        StreamsAdapter.PersonClickListener, StreamsAdapter.StreamStarListener {

    @Inject IntentStarter intentStarter;

    @Override protected int getLayoutRes() {
        return R.layout.fragment_streams_base;
    }

    @Override
    protected ListAdapter<List<Stream>> createAdapter() {
        return new StreamsAdapter(getActivity(), this, this, this);
    }

    @Override
    public void onStreamClicked(StreamsAdapterHolders.StreamViewHolder vh, Stream stream) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        Pair.create((View) vh.senderPic, getString(R.string.shared_stream_sender_pic)),
                        Pair.create((View) vh.subject, getString(R.string.shared_stream_subject)),
                        Pair.create((View) vh.date, getString(R.string.shared_stream_date)),
                        Pair.create((View) vh.star, getString(R.string.shared_stream_star)),
                        Pair.create(getActivity().findViewById(R.id.toolbar),
                                getString(R.string.shared_stream_toolbar)));

        intentStarter.showStreamDetails(getActivity(), stream, options.toBundle());
    }

    @Override
    public void onPersonClicked(Person person) {
        intentStarter.showProfile(getActivity(), person);
    }

    @Override
    public void onStreamStarClicked(Stream stream) {
        presenter.starStream(stream, !stream.isStarred());
    }

    @Override
    public void markStreamAsStared(int streamId) {
        // Search for the stream
        Stream stream = ((StreamsAdapter) adapter).findStream(streamId);
        if (stream != null) {
            stream.setStarred(true);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void markStreamAsUnstared(int streamId) {
        // Search for the stream
        Stream stream = ((StreamsAdapter) adapter).findStream(streamId);
        if (stream != null) {
            stream.setStarred(false);
            adapter.notifyDataSetChanged();
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

    @Override
    public void markStreamAsRead(Stream stream, boolean read) {
        StreamsAdapter.StreamInAdapterResult result = ((StreamsAdapter) adapter).findStream(stream);
        if (result.isFound()) {
            result.getAdapterStream().read(read);
            adapter.notifyDataSetChanged();
        }
    }
}
