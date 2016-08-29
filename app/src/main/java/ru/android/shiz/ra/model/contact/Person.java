package ru.android.shiz.ra.model.contact;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.Date;
import java.util.GregorianCalendar;

import ru.android.shiz.ra.R;

/**
 * Created by kassava on 24.08.2016.
 */
@ParcelablePlease
public class Person implements Parcelable {

    public static final String MAIL_TED = "ted@mosby.com";
    public static final String MAIL_MARSHALL = "marshall@eriksen.com";
    public static final String MAIL_ROBIN = "robin@metronews1.com";
    public static final String MAIL_LILY = "lily@aldrin.com";
    public static final String MAIL_BARNEY = "barney@legendary.me";

    public static final Person TED = new Person(1, "Ted Mosby", Person.MAIL_TED, R.drawable.ted,
            new GregorianCalendar(1978, 3, 25).getTime(), R.string.bio_ted);
    public static final Person MARSHALL =
            new Person(2, "Marshall Eriksen", Person.MAIL_MARSHALL, R.drawable.marshall,
                    new GregorianCalendar(1978, 0, 1).getTime(), R.string.bio_marshall);
    public static final Person ROBIN =
            new Person(3, "Robin Scherbatsky", Person.MAIL_ROBIN, R.drawable.robin,
                    new GregorianCalendar(1980, 6, 23).getTime(), R.string.bio_robin);
    public static final Person LILY = new Person(4, "Lily Aldrin", Person.MAIL_LILY, R.drawable.lily,
            new GregorianCalendar(1978, 0, 1).getTime(), R.string.bio_lily);
    public static final Person BARNEY =
            new Person(5, "Barney Stinson", Person.MAIL_BARNEY, R.drawable.barney,
                    new GregorianCalendar(1974, 0, 1).getTime(), R.string.bio_barney);

    int id;

    String name;

    /**
     * The imageRes profile pic resource
     */
    int imageRes;

    String estream;
    Date birthday;
    int bioRes;

    public Person(int id, String name, String estream, int imageRes, Date birthday, int bioRes) {
        this.id = id;
        this.name = name;
        this.imageRes = imageRes;
        this.estream = estream;
        this.birthday = birthday;
        this.bioRes = bioRes;
    }

    private Person() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getEstream() {
        return estream;
    }

    public Date getBirthday() {
        return birthday;
    }

    public int getBioRes() {
        return bioRes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        PersonParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel source) {
            Person target = new Person();
            PersonParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
