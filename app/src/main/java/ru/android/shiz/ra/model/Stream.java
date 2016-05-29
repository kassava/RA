package ru.android.shiz.ra.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kassava on 28.04.2016.
 */
public class Stream implements Parcelable {

    String header;
    int id;

    private Stream() {
    }

    public Stream(int id, String header) {
        this.id = id;
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return header;
    }

    protected Stream(Parcel in) {
    }

    public static final Creator<Stream> CREATOR = new Creator<Stream>() {
        @Override
        public Stream createFromParcel(Parcel in) {
            return new Stream(in);
        }

        @Override
        public Stream[] newArray(int size) {
            return new Stream[size];
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
