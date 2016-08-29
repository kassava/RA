package ru.android.shiz.ra.model.stream;

import java.util.List;

/**
 * Created by kassava on 24.08.2016.
 */
public interface StreamGenerator {

    public List<Stream> generateStreams();

    public Stream generateResponseStream(String senderMail);
}