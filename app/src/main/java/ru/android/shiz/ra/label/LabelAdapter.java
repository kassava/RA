package ru.android.shiz.ra.label;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.model.stream.Label;
import ru.android.shiz.ra.utils.SimpleAdapter;

/**
 * Created by kassava on 22.08.2016.
 */
public class LabelAdapter extends SimpleAdapter<List<Label>> {

    static class ViewHolder {

        @BindView(R.id.name)
        TextView text;
        @BindView(R.id.icon)
        ImageView icon;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
            icon.setColorFilter(icon.getResources().getColor(R.color.secondary_text));
        }
    }

    public LabelAdapter(Context context) {
        super(context);
    }

    @Override
    public View newView(int type, ViewGroup parent) {
        View view = inflater.inflate(R.layout.list_labelview_item, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    @Override
    public void bindView(int position, int type, View view) {
        ViewHolder vh = (ViewHolder) view.getTag();
        Label label = items.get(position);

        vh.text.setText(label.getName());
        vh.icon.setImageResource(label.getIconRes());
    }
}
