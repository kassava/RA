package ru.android.shiz.ra.streams;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import ru.android.shiz.ra.R;
import ru.android.shiz.ra.base.view.ListAdapter;
import ru.android.shiz.ra.model.contact.Person;
import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.model.stream.StreamComparator;
import ru.android.shiz.ra.ui.view.StarView;

/**
 * Created by kassava on 09.05.2016.
 */
public class StreamsAdapter extends ListAdapter<List<Stream>> implements StreamsAdapterBinder {

    public interface StreamClickedListener {
        public void onStreamClicked(StreamsAdapterHolders.StreamViewHolder vh, Stream stream);
    }

    public interface StreamStarListener {
        public void onStreamStarClicked(Stream stream);
    }

    public interface PersonClickListener {
        public void onPersonClicked(Person person);
    }

    @ViewType(
            layout = R.layout.list_stream_item,
            views = {
                    @ViewField(id = R.id.senderPic, name = "senderPic", type = ImageView.class),
                    @ViewField(id = R.id.subject, name = "subject", type = TextView.class),
                    @ViewField(id = R.id.message, name = "message", type = TextView.class),
                    @ViewField(id = R.id.date, name = "date", type = TextView.class),
                    @ViewField(id = R.id.starButton, name = "star", type = StarView.class)
            }) public final int stream = 0;

    private StreamClickedListener clickListener;
    private StreamStarListener starListner;
    private PersonClickListener personClickListener;
    private Format format = new SimpleDateFormat("dd. MMM", Locale.getDefault());

    public StreamsAdapter(Context context, StreamClickedListener clickListener,
                          StreamStarListener starListener, PersonClickListener personClickListener) {
        super(context);
        this.clickListener = clickListener;
        this.starListner = starListener;
        this.personClickListener = personClickListener;
    }

    @Override
    public void bindViewHolder(final StreamsAdapterHolders.StreamViewHolder vh, int position) {
        final Stream stream = items.get(position);

        vh.senderPic.setImageResource(stream.getSender().getImageRes());
        vh.subject.setText(stream.getSubject());
        vh.message.setText(
                Html.fromHtml(stream.getSender().getName() + " - <i>" + stream.getText() + "</i>"));
        vh.date.setText(format.format(stream.getDate()));
        vh.star.setStarred(stream.isStarred());
        vh.star.clearAnimation();

        if (stream.isRead()) {
            vh.subject.setTypeface(null, Typeface.NORMAL);
            vh.message.setTypeface(null, Typeface.NORMAL);
            vh.date.setTypeface(null, Typeface.NORMAL);
        } else {
            vh.subject.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
            vh.message.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
            vh.date.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
        }

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onStreamClicked(vh, stream);
            }
        });
        vh.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starListner.onStreamStarClicked(stream);
            }
        });
        vh.senderPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personClickListener.onPersonClicked(stream.getSender());
            }
        });
    }

    /**
     * Finds a stream by his id if displayed in this adapter
     */
    public Stream findStream(int id) {
        if (items == null) {
            return null;
        }

        for (Stream s : items) {
            if (s.getId() == id) {
                return s;
            }
        }

        return null;
    }

    /**
     * Searches for an equal stream (compares stream id) in the adapter.
     *
     * @return A {@link StreamInAdapterResult} containing the information if the adapter contains that
     * stream and at which index position. If the adapter doesn't contain this stream, then the result
     * will contain the index position where the stream would be.
     */
    public StreamInAdapterResult findStream(Stream stream) {
        int indexPosition = Collections.binarySearch(items, stream, StreamComparator.INSTANCE);
        boolean containsStream = false;
        Stream found = null;
        if (indexPosition < 0) {
            indexPosition = ~indexPosition;
        } else {
            found = items.get(indexPosition);
            if (found.getId() == stream.getId()) {
                containsStream = true;
            } else {
                containsStream = false;
                found = null;
            }
        }

        return new StreamInAdapterResult(containsStream, found, indexPosition);
    }

    /**
     * Holds the information if the adapter contains a certain stream and at which index position. If
     * the adapter doesn't contain this stream, then the result will
     * contain the index position where the stream would be.
     */
    public static class StreamInAdapterResult {
        boolean found;
        Stream adapterStream;
        int index;

        public StreamInAdapterResult(boolean found, Stream adapterStream, int index) {
            this.found = found;
            this.adapterStream = adapterStream;
            this.index = index;
        }

        public boolean isFound() {
            return found;
        }

        public Stream getAdapterStream() {
            return adapterStream;
        }

        public int getIndex() {
            return index;
        }
    }
}
