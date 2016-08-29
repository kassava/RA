package ru.android.shiz.ra.model.event;

/**
 * Created by kassava on 24.08.2016.
 */
public class StreamStaredEvent {
    private int streamId;

    public StreamStaredEvent(int streamId) {
        this.streamId = streamId;
    }

    public int getStreamId() {
        return streamId;
    }
}
