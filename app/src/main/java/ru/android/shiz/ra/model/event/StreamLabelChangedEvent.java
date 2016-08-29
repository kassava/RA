package ru.android.shiz.ra.model.event;

import ru.android.shiz.ra.model.stream.Stream;

/**
 * This event is fired to inform that the label of a stream has changed.
 *
 * Created by kassava on 24.08.2016.
 */
public class StreamLabelChangedEvent {

    private Stream stream;
    private String label;

    public StreamLabelChangedEvent(Stream stream, String label) {
        this.stream = stream;
        this.label = label;
    }

    public Stream getStream() {
        return stream;
    }

    public String getLabel() {
        return label;
    }
}
