package ru.android.shiz.ra.streams;

import java.util.List;

import ru.android.shiz.ra.base.view.BaseStreamView;
import ru.android.shiz.ra.model.stream.Stream;

/**
 * Created by kassava on 28.04.2016.
 */
public interface StreamsView extends BaseStreamView<List<Stream>> {

    public void changeLabel(Stream stream, String label);
}
