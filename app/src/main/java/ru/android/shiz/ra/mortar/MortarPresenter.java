package ru.android.shiz.ra.mortar;

import android.os.Bundle;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import mortar.ViewPresenter;
import ru.android.shiz.ra.streams.CustomRestorableParcelableLceViewState;
import ru.android.shiz.ra.streams.CustomRestorableParcelableViewState;
import ru.android.shiz.ra.streams.StreamsListLayout;

/**
 * Created by kassava on 05.09.2016.
 */
public class MortarPresenter extends ViewPresenter<StreamsListLayout> {
    private final DateFormat format = new SimpleDateFormat();
    private int serial = -1;

    private CustomRestorableParcelableViewState viewState;

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        if (savedInstanceState != null && serial == -1) serial = savedInstanceState.getInt("serial");

//        getView().show("Update #" + ++serial + " at " + format.format(new Date()) + " --- ");
        Log.d("mortar", "state: " + savedInstanceState.getParcelable("view_state"));
    }

    @Override
    protected void onSave(Bundle outState) {
//        outState.putInt("serial", serial);
        outState.putParcelable("view_state", viewState);
    }

    public void setViewState(CustomRestorableParcelableViewState viewState) {
        this.viewState = viewState;
    }
}
