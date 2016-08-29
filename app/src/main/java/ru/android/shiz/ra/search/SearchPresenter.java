package ru.android.shiz.ra.search;

import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import ru.android.shiz.ra.base.presenter.BaseRxStreamPresenter;
import ru.android.shiz.ra.model.event.LoginSuccessfulEvent;
import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.model.stream.StreamProvider;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kassava on 25.08.2016.
 */
public class SearchPresenter extends BaseRxStreamPresenter<SearchView, List<Stream>> {

    private int queryLimit = 20;
    private Subscriber<List<Stream>> olderSubscriber;

    @Inject
    public SearchPresenter(StreamProvider streamProvider, EventBus eventBus) {
        super(streamProvider, eventBus);
    }

    public void loadOlderStreams(String query, Stream olderAs) {

        // Cancel any previous query
        unsubscribe();

        final Observable<List<Stream>> older =
                streamProvider.searchForOlderStreams(query, olderAs.getDate(), queryLimit);

        if (isViewAttached()) {
            getView().showLoadMore(true);
        }
        olderSubscriber = new Subscriber<List<Stream>>() {
            @Override public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showLoadMoreError(e);
                    getView().showLoadMore(false);
                }
            }

            @Override
            public void onNext(List<Stream> streams) {
                if (isViewAttached()) {
                    getView().addOlderStreams(streams);
                    getView().showLoadMore(false);
                }
            }
        };

        // start
        older.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(olderSubscriber);
    }

    public void searchFor(String query, boolean pullToRefresh) {

        // If searching for empty string, then do nothing
        if (isViewAttached() && TextUtils.isEmpty(query)) {
            unsubscribe();
            getView().showSearchNotStartedYet();
            return;
        }

        // in case the previous action was load more we have to reset the view
        if (isViewAttached()) {
            getView().showLoadMore(false);
        }

        subscribe(streamProvider.searchForStreams(query, queryLimit), pullToRefresh);
    }

    @Override
    protected void unsubscribe() {
        super.unsubscribe();
        if (olderSubscriber != null && !olderSubscriber.isUnsubscribed()) {
            olderSubscriber.unsubscribe();
        }
    }

    public void onEventMainThread(LoginSuccessfulEvent event) {
        if (isViewAttached()) {
            getView().showSearchNotStartedYet();
        }
    }
}
