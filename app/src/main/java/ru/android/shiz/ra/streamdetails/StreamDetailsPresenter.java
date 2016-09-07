package ru.android.shiz.ra.streamdetails;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import ru.android.shiz.ra.model.RaApi;
import ru.android.shiz.ra.model.StreamDetail;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kassava on 06.09.16.
 */
public class StreamDetailsPresenter extends MvpBasePresenter<StreamsDetailsView> {

    private final String LOG_TAG = StreamDetailsPresenter.class.getSimpleName();

    Subscription subscription = null;
    private RaApi raApi;

    @Inject
    public StreamDetailsPresenter(RaApi raApi) {
        this.raApi = raApi;
    }

    public void loadDetails(int id) {
        Log.d(LOG_TAG, "loadDetails");

        if (isViewAttached()) {
            getView().showLoading(false);
        }

        subscription = raApi.getDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StreamDetail>() {
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
                            getView().showError(e, false);
                        }
                    }

                    @Override
                    public void onNext(StreamDetail streamDetails) {
                        Log.d(LOG_TAG, "view.onNext(): " + streamDetails.toString());
                        if (isViewAttached()) {
                            getView().setData(streamDetails);
                        }
                    }
                });
    }

    @Override
    public void attachView(StreamsDetailsView view) {
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
