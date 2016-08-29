package ru.android.shiz.ra.model.stream;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.Date;

import ru.android.shiz.ra.model.contact.Person;

/**
 * Created by kassava on 28.04.2016.
 */
@ParcelablePlease
public class Stream implements Parcelable {

    int id;
    Person sender;
    Person receiver;

    String subject;
    Date date;
    boolean read;
    String text;

    String label;
    boolean starred;


    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        StreamParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<Stream> CREATOR = new Creator<Stream>() {
        public Stream createFromParcel(Parcel source) {
            Stream target = new Stream();
            StreamParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public Stream[] newArray(int size) {
            return new Stream[size];
        }
    };

    public Person getSender() {
        return sender;
    }

    public Person getReceiver() {
        return receiver;
    }

    public String getSubject() {
        return subject;
    }

    public Date getDate() {
        return date;
    }

    public boolean isRead() {
        return read;
    }

    public String getText() {
        return text;
    }

    public String getLabel() {
        return label;
    }

    public Stream sender(Person s){
        this.sender = s;
        return this;
    }

    public Stream receiver(Person r){
        this.receiver = r;
        return this;
    }

    public Stream subject(String s){
        this.subject = s;
        return this;
    }

    public Stream date(Date d){
        this.date = d;
        return this;
    }

    public Stream read(boolean read){
        this.read = read;
        return this;
    }

    public Stream text(String text){
        this.text = text;
        return this;
    }

    public Stream label(String label){
        this.label = label;
        return this;
    }

    public Stream id(int id){
        this.id = id;
        return this;
    }

    public int getId() {
        return id;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

}