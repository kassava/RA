package ru.android.shiz.ra.model.account;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import ru.android.shiz.ra.R;
import ru.android.shiz.ra.model.contact.Person;

/**
 * Created by kassava on 24.08.2016.
 */
@ParcelablePlease
public class Account implements Parcelable {

    String name = "Ted Mosby";
    String email = Person.MAIL_TED;
    int imageRes = R.drawable.ted;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getImageRes() {
        return imageRes;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        AccountParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        public Account createFromParcel(Parcel source) {
            Account target = new Account();
            AccountParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
