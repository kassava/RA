package ru.android.shiz.ra.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by kassava on 06.09.16.
 */
public class StreamDetail implements Parcelable {

    private int id;
    private String imageUrl;
    private List<Info> infos;

    private StreamDetail() {
    }

    public StreamDetail(int id, String imageUrl, List<Info> infos) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.infos = infos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String header) {
        this.imageUrl = header;
    }


    public List<Info> getInfos() {
        return infos;
    }

    public void setInfos(List<Info> infos) {
        this.infos = infos;
    }

    protected StreamDetail(Parcel in) {
    }

    public static final Creator<StreamDetail> CREATOR = new Creator<StreamDetail>() {
        @Override
        public StreamDetail createFromParcel(Parcel in) {
            return new StreamDetail(in);
        }

        @Override
        public StreamDetail[] newArray(int size) {
            return new StreamDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
