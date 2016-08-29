package ru.android.shiz.ra.search;

import java.util.List;

import ru.android.shiz.ra.base.view.BaseStreamView;
import ru.android.shiz.ra.model.stream.Stream;

/**
 * Created by kassava on 25.08.2016.
 */
public interface SearchView extends BaseStreamView<List<Stream>> {

    public void addOlderStreams(List<Stream> older);

    public void showLoadMore(boolean showLoadMore);

    public void showLoadMoreError(Throwable e);

    public void showSearchNotStartedYet();
}
