package ru.android.shiz.ra.broadcastpreview;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * Created by kassava on 12.09.16.
 */
@ParcelablePlease
public class BroadcastPreviewScreen implements Parcelable {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        BroadcastPreviewScreenParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<BroadcastPreviewScreen> CREATOR = new Creator<BroadcastPreviewScreen>() {
        public BroadcastPreviewScreen createFromParcel(Parcel source) {
            BroadcastPreviewScreen target = new BroadcastPreviewScreen();
            BroadcastPreviewScreenParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public BroadcastPreviewScreen[] newArray(int size) {
            return new BroadcastPreviewScreen[size];
        }
    };
}
