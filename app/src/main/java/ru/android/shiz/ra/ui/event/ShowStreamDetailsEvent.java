package ru.android.shiz.ra.ui.event;

import ru.android.shiz.ra.model.stream.Stream;

/**
 * Event to inform that a certain
 * Created by kassava on 25.08.2016.
 */
public class ShowStreamDetailsEvent {
    private Stream stream;

    public ShowStreamDetailsEvent(Stream stream) {
        this.stream = stream;
    }

    public Stream getStream() {
        return stream;
    }
}
