package ru.android.shiz.ra.streams;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kassava on 24.04.2016.
 */
public class StreamsScreen implements Parcelable {

    public static final Creator<StreamsScreen> CREATOR = new Creator<StreamsScreen>() {
        @Override
        public StreamsScreen createFromParcel(Parcel in) {
            return new StreamsScreen();
        }

        @Override
        public StreamsScreen[] newArray(int size) {
            return new StreamsScreen[size];
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
