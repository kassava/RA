package ru.android.shiz.ra.model.stream;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * Created by kassava on 24.08.2016.
 */
@ParcelablePlease
public class Label implements Parcelable {

    public static final String INBOX = "Inbox";

    public static final String TRASH = "Trash";

    public static final String SENT = "Sent";

    public static final String SPAM = "Spam";

    String name;
    int iconRes;
    int unreadCount;

    public Label(String name, int iconRes, int unreadCount) {
        this.name = name;
        this.iconRes = iconRes;
        this.unreadCount = unreadCount;
    }

    private Label(){

    }

    public String getName() {
        return name;
    }

    public int getIconRes() {
        return iconRes;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public void decrementUnreadCount(){
        unreadCount--;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        LabelParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<Label> CREATOR = new Creator<Label>() {
        public Label createFromParcel(Parcel source) {
            Label target = new Label();
            LabelParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public Label[] newArray(int size) {
            return new Label[size];
        }
    };
}
