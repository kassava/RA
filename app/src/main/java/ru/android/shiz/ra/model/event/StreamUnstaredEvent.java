package ru.android.shiz.ra.model.event;

/**
 * Created by kassava on 24.08.2016.
 */
public class StreamUnstaredEvent {
    private int streamId;

    public StreamUnstaredEvent(int streamId) {
        this.streamId = streamId;
    }

    public int getStreamId() {
        return streamId;
    }
}