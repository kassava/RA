package ru.android.shiz.ra.label;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;

import java.util.List;

import ru.android.shiz.ra.model.stream.Label;

/**
 * Created by kassava on 22.08.2016.
 */
public class LabelViewState extends CastedArrayListLceViewState<List<Label>, LabelView> {

    public static final Parcelable.Creator<LabelViewState> CREATOR = new Parcelable.Creator<LabelViewState>() {
        @Override
        public LabelViewState createFromParcel(Parcel source) {
            return new LabelViewState(source);
        }

        @Override
        public LabelViewState[] newArray(int size) {
            return new LabelViewState[size];
        }
    };

    private final int STATE_SHOWING_LABEL = 3;

    public LabelViewState() {
    }

    protected LabelViewState(Parcel source) {
        super(source);
    }

    public void setStateShowingLabel() {
        currentViewState = STATE_SHOWING_LABEL;
    }

    @Override
    public void apply(LabelView view, boolean retained) {

        if (currentViewState == STATE_SHOWING_LABEL) {
            view.showLabel();
        } else {
            super.apply(view, retained);
        }
    }
}
