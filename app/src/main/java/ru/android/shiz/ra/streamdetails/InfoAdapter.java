package ru.android.shiz.ra.streamdetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.model.Info;
import ru.android.shiz.ra.model.InfoPicture;
import ru.android.shiz.ra.model.InfoText;

/**
 * Created by kassava on 06.09.16.
 */
public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEWTYPE_TEXT = 0;
    private final int VIEWTYPE_IMAGE = 1;
    private LayoutInflater inflater;

    List<Info> items = null;

    public InfoAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch(viewType) {
            case VIEWTYPE_IMAGE:
                viewHolder = InfoPictureViewHolder(inflater.inflate(R.layout.item_picture, parent, false));
                break;
            case VIEWTYPE_TEXT:
                viewHolder = InfoViewHolder(inflater.inflate(R.layout.item_info, parent, false));
                break;
            default:
//                throw new Exception("unkonown viewholder");
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InfoViewHolder) {
            InfoText info = (InfoText) items.get(position);
            holder.title.setText(info.getTitleRes());
            holder.text.setText(info.getText());
        } else {
            if (holder instanceof InfoPictureViewHolder) {
                InfoPicture info = (InfoPicture) items.get(position);
                // download image and set it to holder ...
            } else {
                // throw new Exception("unkonown viewholder");
            }
        }
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        } else {
            return items.size();
        }
    }

    public static class InfoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView title;
        @BindView(R.id.text) TextView text;

        public InfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public String toString() {
            return "InfoViewHolder " + super.toString();
        }
    }

    public static class InfoPictureViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image) ImageView image;

        public InfoPictureViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public String toString() {
            return "InfoPictureViewHolder " + super.toString();
        }
    }
}