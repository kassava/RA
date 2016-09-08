package ru.android.shiz.ra.mortar;

import android.os.Bundle;
import android.util.Log;

import mortar.ViewPresenter;
import ru.android.shiz.ra.broadcastdetails.BroadcastDetailsLayout;
import ru.android.shiz.ra.base.viewstate.CustomRestorableParcelableViewState;

/**
 * Created by kassava on 07.09.16.
 */
public class DetailsMortarPresenter extends ViewPresenter<BroadcastDetailsLayout> {

    private CustomRestorableParcelableViewState viewState;

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Log.d("mortar", "state: " + savedInstanceState.getParcelable("view_state"));
        }
    }

    @Override
    protected void onSave(Bundle outState) {
        outState.putParcelable("view_state", viewState);
    }

    public void setViewState(CustomRestorableParcelableViewState viewState) {
        this.viewState = viewState;
    }

    public CustomRestorableParcelableViewState getViewState() {
        return viewState;
    }
}
