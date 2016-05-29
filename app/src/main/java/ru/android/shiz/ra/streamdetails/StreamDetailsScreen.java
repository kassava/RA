package ru.android.shiz.ra.streamdetails;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kassava on 24.04.2016.
 */
public class StreamDetailsScreen implements Parcelable {

    private int streamId;

    public StreamDetailsScreen(int streamId) {
        this.streamId = streamId;
    }

    private StreamDetailsScreen(Parcel in) {
        this.streamId = in.readInt();
    }

    public static final Creator<StreamDetailsScreen> CREATOR = new Creator<StreamDetailsScreen>() {
        @Override
        public StreamDetailsScreen createFromParcel(Parcel in) {
            return new StreamDetailsScreen(in);
        }

        @Override
        public StreamDetailsScreen[] newArray(int size) {
            return new StreamDetailsScreen[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(streamId);
    }
}
