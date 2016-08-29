package ru.android.shiz.ra.menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;

import java.util.List;

import ru.android.shiz.ra.R;
import ru.android.shiz.ra.base.view.ListAdapter;
import ru.android.shiz.ra.model.stream.Label;

/**
 * Created by kassava on 24.08.2016.
 */
public class MenuAdapter extends ListAdapter<List<Label>> implements MenuAdapterBinder {

    public interface LabelClickListener {
        public void onLabelClicked(Label label);

        public void onStatisticsClicked();
    }

    @ViewType(layout = R.layout.list_menu_item,
            initMethod = true,
            views = {
                    @ViewField(id = R.id.icon, name = "icon", type = ImageView.class),
                    @ViewField(id = R.id.name, name = "name", type = TextView.class),
                    @ViewField(id = R.id.unreadCount, name = "unread", type = TextView.class)
            }) public final int menuItem = 0;

    @ViewType(layout = R.layout.list_menu_statistics,
            initMethod = true,
            views = @ViewField(id = R.id.icon, name = "icon", type = ImageView.class)) public final int
            statisticsItem = 1;

    private Context context;
    private LabelClickListener listener;

    public MenuAdapter(Context context, LabelClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        if (items == null || items.size() == 0) {
            return 0;
        } else {
            return items.size() + 1; // +1 because of statistics item
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? statisticsItem : menuItem;
    }

    @Override
    public void initViewHolder(MenuAdapterHolders.MenuItemViewHolder vh, View view,
                                         ViewGroup parent) {
        vh.icon.setColorFilter(context.getResources().getColor(R.color.secondary_text));
    }

    @Override
    public void bindViewHolder(MenuAdapterHolders.MenuItemViewHolder vh, int position) {
        final Label label = items.get(position);
        vh.icon.setImageResource(label.getIconRes());
        vh.name.setText(label.getName());

        if (label.getUnreadCount() > 0) {
            vh.unread.setVisibility(View.VISIBLE);
            vh.unread.setText("" + label.getUnreadCount());
        } else {
            vh.unread.setVisibility(View.INVISIBLE);
        }

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLabelClicked(label);
            }
        });
    }

    @Override
    public void initViewHolder(MenuAdapterHolders.StatisticsItemViewHolder vh, View view,
                                         ViewGroup parent) {
        vh.icon.setColorFilter(context.getResources().getColor(R.color.secondary_text));
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStatisticsClicked();
            }
        });
    }

    @Override
    public void bindViewHolder(MenuAdapterHolders.StatisticsItemViewHolder vh, int position) {

    }
}
