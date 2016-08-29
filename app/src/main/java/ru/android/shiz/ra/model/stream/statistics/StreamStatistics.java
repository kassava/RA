package ru.android.shiz.ra.model.stream.statistics;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.ArrayList;

/**
 * Created by kassava on 24.08.2016.
 */
@ParcelablePlease
public class StreamStatistics implements Parcelable {

    ArrayList<StreamsCount> streamsCounts;

    public StreamStatistics(ArrayList<StreamsCount> streamsCounts) {
        this.streamsCounts = streamsCounts;
    }

    public StreamStatistics() {
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        StreamStatisticsParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<StreamStatistics> CREATOR = new Parcelable.Creator<StreamStatistics>() {
        public StreamStatistics createFromParcel(Parcel source) {
            StreamStatistics target = new StreamStatistics();
            StreamStatisticsParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public StreamStatistics[] newArray(int size) {
            return new StreamStatistics[size];
        }
    };

    public ArrayList<StreamsCount> getStreamsCounts() {
        return streamsCounts;
    }

    public void setStreamsCounts(ArrayList<StreamsCount> streamsCounts) {
        this.streamsCounts = streamsCounts;
    }
}
