package ru.android.shiz.ra.model.event;

import ru.android.shiz.ra.model.stream.Stream;

/**
 * Created by kassava on 24.08.2016.
 */
public class StreamReadEvent {

    boolean read;
    Stream stream;

    public StreamReadEvent(Stream stream, boolean read) {
        this.stream = stream;
    }

    public Stream getStream() {
        return stream;
    }

    public boolean isRead() {
        return read;
    }
}
