package ru.android.shiz.ra.model.stream;

import java.util.Comparator;

/**
 * A Comparator for sorting mails in reverse order. Useful for binary search.
 *
 * Created by kassava on 24.08.2016.
 */
public class StreamComparator implements Comparator<Stream> {


    public static final  StreamComparator INSTANCE = new StreamComparator();

    private StreamComparator(){

    }

    @Override
    public int compare(Stream lhs, Stream rhs) {
        return rhs.getDate().compareTo(lhs.getDate());
    }
}
