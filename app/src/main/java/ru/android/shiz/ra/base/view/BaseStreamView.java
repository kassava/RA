package ru.android.shiz.ra.base.view;

import ru.android.shiz.ra.model.stream.Stream;

/**
 * Created by kassava on 21.08.2016.
 */
public interface BaseStreamView<M> extends AuthView<M> {

    public void markStreamAsStared(int mailId);

    public void markStreamAsUnstared(int streamId);

    public void showStaringFailed(Stream stream);

    public void showUnstaringFailed(Stream stream);


    /**
     * Marks a certain stream as read
     */
    public void markStreamAsRead(Stream stream, boolean read);

}
