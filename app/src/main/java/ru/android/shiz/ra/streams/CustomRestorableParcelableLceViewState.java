package ru.android.shiz.ra.streams;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableParcelableViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.AbsLceViewState;

import java.util.List;

import ru.android.shiz.ra.model.Stream;

/**
 * Created by kassava on 30.08.16.
 */
@SuppressLint("ParcelCreator")
public class CustomRestorableParcelableLceViewState<D, V extends MvpLceView<D>> extends AbsLceViewState<D, V>
        implements RestorableParcelableViewState<V> {

    private static final String LOG_TAG = CustomRestorableParcelableLceViewState.class.getSimpleName();

    public static final String KEY_BUNDLE_VIEW_STATE =
            "ru.android.shiz.ra.streams.ViewState.bundlekey";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(currentViewState);

        // PullToRefresh
        writeBoolean(dest, pullToRefresh);

        // write exception
        dest.writeSerializable(exception);
    }

    protected void readFromParcel(Parcel in) {
        currentViewState = in.readInt();

        // Pull To Refresh
        pullToRefresh = readBoolean(in);

        exception = (Throwable) in.readSerializable();

        // content will be read in subclass
    }

    protected void writeBoolean(Parcel dest, boolean b) {
        dest.writeByte((byte) (b ? 1 : 0));
    }

    protected boolean readBoolean(Parcel p) {
        return p.readByte() == (byte) 1;
    }

    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        Log.d(LOG_TAG, "saveInstanceState");

        out.putParcelable(KEY_BUNDLE_VIEW_STATE, this);
    }

    @Override
    public RestorableViewState<V> restoreInstanceState(Bundle in) {
        Log.d(LOG_TAG, "restoreInstanceState");

        if (in == null) {
            return null;
        }

        // Workaround to solve class loader problem.
        // But it returns a copy of the view state and not this viewstate. However, that's ok!
        return (CustomRestorableParcelableLceViewState<D, V>) in.getParcelable(KEY_BUNDLE_VIEW_STATE);
    }
}
