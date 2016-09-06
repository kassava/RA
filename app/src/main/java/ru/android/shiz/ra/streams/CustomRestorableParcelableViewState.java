package ru.android.shiz.ra.streams;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableParcelableViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;

/**
 * Created by kassava on 01.09.2016.
 */
public class CustomRestorableParcelableViewState<D, V extends MvpLceView<D>>
        implements RestorableParcelableViewState<V>, LceViewState<D, V> {

    private final String LOG_TAG = CustomRestorableParcelableViewState.class.getSimpleName();

    public static final String KEY_BUNDLE_VIEW_STATE =
            "ru.android.shiz.ra.streams.ViewState.bundlekey";

    protected int currentViewState;
    protected boolean pullToRefresh;
    protected Throwable exception;
    protected D loadedData;

    public static final Parcelable.Creator<CustomRestorableParcelableViewState> CREATOR =
            new Parcelable.Creator<CustomRestorableParcelableViewState>() {
                public CustomRestorableParcelableViewState createFromParcel(Parcel in) {
                    return new CustomRestorableParcelableViewState(in);
                }

                public CustomRestorableParcelableViewState[] newArray(int size) {
                    return new CustomRestorableParcelableViewState[size];
                }
            };

    public CustomRestorableParcelableViewState() {
    }

    public CustomRestorableParcelableViewState(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getCurrentViewState() {
        return currentViewState;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(currentViewState);

        // PullToRefresh
        writeBoolean(dest, pullToRefresh);

        // write exception
        dest.writeSerializable(exception);
    }

    protected void writeBoolean(Parcel dest, boolean b) {
        dest.writeByte((byte) (b ? 1 : 0));
    }

    protected void readFromParcel(Parcel in) {
        currentViewState = in.readInt();

        // Pull To Refresh
        pullToRefresh = readBoolean(in);

        exception = (Throwable) in.readSerializable();
    }

    protected boolean readBoolean(Parcel p) {
        return p.readByte() == (byte) 1;
    }

    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        Log.d(LOG_TAG, "saveInstanceState: " + out);

    }

    @Override
    public RestorableViewState<V> restoreInstanceState(Bundle in) {
        Log.d(LOG_TAG, "restoreInstanceState: " + in);

        if (in == null) {
            return null;
        }

        // Workaround to solve class loader problem.
        // But it returns a copy of the view state and not this viewstate. However, that's ok!
        return (CustomRestorableParcelableViewState<D, V>) in.getParcelable(KEY_BUNDLE_VIEW_STATE);
    }

    @Override
    public void apply(V view, boolean retained) {
        if (currentViewState == STATE_SHOW_CONTENT) {
            view.setData(loadedData);
            view.showContent();
        } else if (currentViewState == STATE_SHOW_LOADING) {

            boolean ptr = pullToRefresh;
            if (pullToRefresh) {
                view.setData(loadedData);
                view.showContent();
            }

            if (retained) {
                view.showLoading(ptr);
            } else {
                view.loadData(ptr);
            }
        } else if (currentViewState == STATE_SHOW_ERROR) {

            boolean ptr = pullToRefresh;
            Throwable e = exception;
            if (pullToRefresh) {
                view.setData(loadedData);
                view.showContent();
            }
            view.showError(e, ptr);
        }
    }

    @Override
    public void setStateShowContent(D loadedData) {
        currentViewState = STATE_SHOW_CONTENT;
        this.loadedData = loadedData;
        exception = null;
    }

    @Override
    public void setStateShowError(Throwable e, boolean pullToRefresh) {
        currentViewState = STATE_SHOW_ERROR;
        exception = e;
        this.pullToRefresh = pullToRefresh;
        if (!pullToRefresh) {
            loadedData = null;
        }
    }

    @Override
    public void setStateShowLoading(boolean pullToRefresh) {
        currentViewState = STATE_SHOW_LOADING;
        this.pullToRefresh = pullToRefresh;
        exception = null;

        if (!pullToRefresh) {
            loadedData = null;
        }
    }
}
