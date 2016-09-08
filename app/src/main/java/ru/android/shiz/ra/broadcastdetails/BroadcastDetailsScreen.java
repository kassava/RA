package ru.android.shiz.ra.broadcastdetails;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kassava on 24.04.2016.
 */
public class BroadcastDetailsScreen implements Parcelable {

    public int getStreamId() {
        return streamId;
    }

    private int streamId;

    public BroadcastDetailsScreen(int streamId) {
        this.streamId = streamId;
    }

    private BroadcastDetailsScreen(Parcel in) {
        this.streamId = in.readInt();
    }

    public static final Creator<BroadcastDetailsScreen> CREATOR = new Creator<BroadcastDetailsScreen>() {
        @Override
        public BroadcastDetailsScreen createFromParcel(Parcel in) {
            return new BroadcastDetailsScreen(in);
        }

        @Override
        public BroadcastDetailsScreen[] newArray(int size) {
            return new BroadcastDetailsScreen[size];
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
