package ru.android.shiz.ra.search;

import android.content.Context;

import com.hannesdorfmann.annotatedadapter.annotation.ViewType;

import java.util.List;

import ru.android.shiz.ra.R;
import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.streams.StreamsAdapter;

/**
 * Created by kassava on 25.08.2016.
 */
public class SearchResultAdapter extends StreamsAdapter implements SearchResultAdapterBinder {

    @ViewType(
            layout = R.layout.list_load_more) public final int loadMore = 1;

    private boolean showLoadMore = false;

    public SearchResultAdapter(Context context, StreamClickedListener clickListener,
                               StreamStarListener starListener, PersonClickListener personClickListener) {
        super(context, clickListener, starListener, personClickListener);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (showLoadMore ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {

        if (showLoadMore && position == items.size()) { // At last position add one
            return loadMore;
        }

        return super.getItemViewType(position);
    }

    public void setLoadMore(boolean enabled) {

        if (showLoadMore != enabled) {

            if (showLoadMore) {
                showLoadMore = false;
                notifyItemRemoved(items.size()); // Remove last position
            } else {
                showLoadMore = true;
                notifyItemInserted(items.size());
            }
        }
    }

    public void addOlderStreams(List<Stream> olderStreams) {
        if (!olderStreams.isEmpty()) {
            int startPosition = items.size();
            items.addAll(olderStreams);
            notifyItemRangeInserted(startPosition, olderStreams.size());
        }
    }

    @Override
    public void bindViewHolder(SearchResultAdapterHolders.LoadMoreViewHolder vh, int position) {
        // Nothing to bind
    }

    public Stream getLastStreamInList() {
        return items == null ? null : items.get(items.size() - 1);
    }
}
