package ru.android.shiz.ra.statistics;

import android.content.Context;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;

import java.util.List;

import ru.android.shiz.ra.R;
import ru.android.shiz.ra.base.view.ListAdapter;
import ru.android.shiz.ra.model.stream.statistics.StreamsCount;

/**
 * Created by kassava on 25.08.2016.
 */
public class StatisticsAdapter extends ListAdapter<List<StreamsCount>>
        implements StatisticsAdapterBinder {

    @ViewType(
            layout = R.layout.list_statistics,
            views = @ViewField(id = R.id.text, name = "text", type = TextView.class)) public final int
            statisticItem = 0;

    public StatisticsAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindViewHolder(StatisticsAdapterHolders.StatisticItemViewHolder vh, int position) {
        StreamsCount count = items.get(position);
        vh.text.setText(count.getStreamsCount() + " mails in " + count.getLabel());
    }
}
