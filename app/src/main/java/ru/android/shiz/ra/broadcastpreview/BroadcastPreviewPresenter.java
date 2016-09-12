package ru.android.shiz.ra.broadcastpreview;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import ru.android.shiz.ra.model.RaApi;

/**
 * Created by kassava on 12.09.16.
 */
public class BroadcastPreviewPresenter extends MvpBasePresenter<BroadcastPreviewView> {

    private final String LOG_TAG = BroadcastPreviewPresenter.class.getSimpleName();

    private RaApi raApi;

    @Inject
    public BroadcastPreviewPresenter(RaApi raApi) {
        this.raApi = raApi;
    }

    public void send() {
        Log.d(LOG_TAG, "send");
    }
}
