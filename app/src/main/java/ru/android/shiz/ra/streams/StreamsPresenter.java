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

    Subscription subscription = null;

    private RAApi raApp;

    @Inject
    public StreamsPresenter(RAApi raApp) {
        this.raApp = raApp;
    }

    public void loadStreams(final boolean pullToRefresh) {
        Log.d("Flow", "loadStreams(" + pullToRefresh + ")");

        if (isViewAttached()) {
            getView().showLoading(pullToRefresh);
        }

        subscription = raApp.getStreams()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Stream>>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            Log.d("Flow", "view.showContent()");
                            getView().showContent();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            Log.d("Flow", "view.showError()");
                            getView().showError(e, pullToRefresh);
                        }
                    }

                    @Override
                    public void onNext(List<Stream> streams) {
                        Log.d("Flow", "view.onNext()");
                        if (isViewAttached()) {
                            getView().setData(streams);
                        }
                    }
                });
    }

    @Override
    public void attachView(StreamsView view) {
        super.attachView(view);
        Log.d("Flow", "attaching View to " + this);
    }

    @Override
    public void detachView(boolean retainInstance) {
        Log.d("Flow", "detaching View from " + this + " retained: " + retainInstance);

        super.detachView(retainInstance);
        if (!retainInstance) {
            subscription.unsubscribe();
        }
    }
}
