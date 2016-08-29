package ru.android.shiz.ra.model.stream.statistics;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * Created by kassava on 24.08.2016.
 */
@ParcelablePlease
public class StreamsCount implements Parcelable {

    String label;
    int streamsCount;

    public StreamsCount(String label, int streamsCount) {
        this.label = label;
        this.streamsCount = streamsCount;
    }

    private StreamsCount() {
    }

    public String getLabel() {
        return label;
    }

    public int getStreamsCount() {
        return streamsCount;
    }

    public void incrementCount(){
        streamsCount++;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        StreamsCountParcelablePlease.writeToParcel(this, dest, flags);
    }


    public static final Creator<StreamsCount> CREATOR = new Creator<StreamsCount>() {
        public StreamsCount createFromParcel(Parcel source) {
            StreamsCount target = new StreamsCount();
            StreamsCountParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public StreamsCount[] newArray(int size) {
            return new StreamsCount[size];
        }
    };
}
