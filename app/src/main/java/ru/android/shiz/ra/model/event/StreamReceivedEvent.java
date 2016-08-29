package ru.android.shiz.ra.model.event;

import ru.android.shiz.ra.model.stream.Stream;

/**
 * Created by kassava on 24.08.2016.
 */
public class StreamReceivedEvent {

    private Stream stream;

    public StreamReceivedEvent(Stream stream) {
        this.stream = stream;
    }

    public Stream getStream() {
        return stream;
    }
}
