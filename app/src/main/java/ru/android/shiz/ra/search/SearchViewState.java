package ru.android.shiz.ra.search;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import ru.android.shiz.ra.base.view.viewstate.AuthCastedArrayListViewState;
import ru.android.shiz.ra.model.stream.Stream;

/**
 * Created by kassava on 25.08.2016.
 */
public class SearchViewState extends AuthCastedArrayListViewState<List<Stream>, SearchView> {

    public static final Parcelable.Creator<SearchViewState> CREATOR = new Parcelable.Creator<SearchViewState>() {
        @Override public SearchViewState createFromParcel(Parcel source) {
            return new SearchViewState(source);
        }

        @Override public SearchViewState[] newArray(int size) {
            return new SearchViewState[size];
        }
    };

    /**
     * This will be the initial state. Not searched for mails yet, therefore not show an error and
     * not
     * show loading and not show content
     */
    public static final int STATE_SHOW_SEARCH_NOT_STARTED = 5;

    boolean loadingMore = false;

    public SearchViewState() {
    }

    protected SearchViewState(Parcel source) {
        super(source);
    }

    public void setLoadingMore(boolean loadingMore) {
        this.loadingMore = loadingMore;
    }

    public void setShowSearchNotStarted() {
        currentViewState = STATE_SHOW_SEARCH_NOT_STARTED;
        loadedData = null;
        exception = null;
    }

    @Override public void apply(SearchView view, boolean retained) {
        if (currentViewState == STATE_SHOW_SEARCH_NOT_STARTED) {
            view.showSearchNotStartedYet();
        } else {

            super.apply(view, retained);

            if (currentViewState == STATE_SHOW_CONTENT) {
                view.showLoadMore(loadingMore);
            }
        }
    }
}
