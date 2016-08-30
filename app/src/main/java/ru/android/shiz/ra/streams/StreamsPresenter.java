package ru.android.shiz.ra.streams;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import ru.android.shiz.ra.model.RAApi;
import ru.android.shiz.ra.model.Stream;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kassava on 28.04.2016.
 */
public class StreamsPresenter extends MvpBasePresenter<StreamsView> {

    private final String LOG_TAG = StreamsPresenter.class.getSimpleName();

    Subscription subscription = null;

    private RAApi raApp;

    @Inject
    public StreamsPresenter(RAApi raApp) {
        this.raApp = raApp;
    }

    public void loadStreams(final boolean pullToRefresh) {
        Log.d(LOG_TAG, "loadStreams(" + pullToRefresh + ")");

        if (isViewAttached()) {
            Log.d(LOG_TAG, "isViewAttached");

            getView().showLoading(pullToRefresh);
        }

        subscription = raApp.getStreams()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Stream>>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            Log.d(LOG_TAG, "view.showContent()!");
                            getView().showContent();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            Log.d(LOG_TAG, "view.showError()");
                            getView().showError(e, pullToRefresh);
                        }
                    }

                    @Override
                    public void onNext(List<Stream> streams) {
                        Log.d(LOG_TAG, "view.onNext(): " + streams.size());
                        if (isViewAttached()) {
                            getView().setData(streams);
                        }
                    }
                });
    }

    @Override
    public void attachView(StreamsView view) {
        super.attachView(view);
        Log.d(LOG_TAG, "attaching View to " + this);
    }

    @Override
    public void detachView(boolean retainInstance) {
        Log.d(LOG_TAG, "detaching View from " + this + " retained: " + retainInstance);

        super.detachView(retainInstance);
        if (!retainInstance && subscription != null) {
            subscription.unsubscribe();
        }
    }
}
