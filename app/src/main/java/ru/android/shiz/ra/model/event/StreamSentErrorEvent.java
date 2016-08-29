package ru.android.shiz.ra.model.event;

import ru.android.shiz.ra.model.stream.Stream;

/**
 * Created by kassava on 24.08.2016.
 */
public class StreamSentErrorEvent {

    private Stream stream;
    private Throwable exception;

    public StreamSentErrorEvent(Stream stream, Throwable exception) {
        this.stream = stream;
        this.exception = exception;
    }

    public Stream getStream() {
        return stream;
    }

    public Throwable getException() {
        return exception;
    }
}
