package ru.android.shiz.ra.broadcasts;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kassava on 24.04.2016.
 */
public class BroadcastsScreen implements Parcelable {

    public static final Creator<BroadcastsScreen> CREATOR = new Creator<BroadcastsScreen>() {
        @Override
        public BroadcastsScreen createFromParcel(Parcel in) {
            return new BroadcastsScreen();
        }

        @Override
        public BroadcastsScreen[] newArray(int size) {
            return new BroadcastsScreen[size];
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
