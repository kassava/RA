package ru.android.shiz.ra.base;

/*
 * Copyright 2015 Hannes Dorfmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.delegate.ViewGroupMvpViewStateDelegateImpl;
import com.hannesdorfmann.mosby.mvp.delegate.ViewGroupMvpDelegate;
import com.hannesdorfmann.mosby.mvp.delegate.MvpViewStateViewGroupDelegateCallback;
import com.hannesdorfmann.mosby.mvp.layout.MvpFrameLayout;
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableParcelableViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

/**
 * A {@link MvpFrameLayout} with ViewState support.
 *
 * @author Hannes Dorfmann
 * @since 1.1
 */
public abstract class MvpViewStateFrameLayout<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpFrameLayout<V, P> implements MvpViewStateViewGroupDelegateCallback<V, P> {

    private boolean restoringViewState = false;
    protected RetainingLceViewState viewState;

    public MvpViewStateFrameLayout(Context context) {
        super(context);
    }

    public MvpViewStateFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MvpViewStateFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public MvpViewStateFrameLayout(Context context, AttributeSet attrs, int defStyleAttr,
                                   int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected ViewGroupMvpDelegate<V, P> getMvpDelegate() {
        if (mvpDelegate == null) {
            mvpDelegate = new ViewGroupMvpViewStateDelegateImpl<V, P>(this);
        }

        return mvpDelegate;
    }

    @SuppressLint("MissingSuperCall")
    @Override protected Parcelable onSaveInstanceState() {
        return ((ViewGroupMvpViewStateDelegateImpl) getMvpDelegate()).onSaveInstanceState();
    }

    @SuppressLint("MissingSuperCall")
    @Override protected void onRestoreInstanceState(Parcelable state) {
        ((ViewGroupMvpViewStateDelegateImpl) getMvpDelegate()).onRestoreInstanceState(state);
    }

    @Override public RetainingLceViewState getViewState() {
        return viewState;
    }

    @Override public void setViewState(ViewState viewState) {
        this.viewState = (RetainingLceViewState) viewState;
    }

    @Override public void setRestoringViewState(boolean retstoringViewState) {
        this.restoringViewState = retstoringViewState;
    }

    @Override public boolean isRestoringViewState() {
        return restoringViewState;
    }

    @Override public void onViewStateInstanceRestored(boolean instanceStateRetained) {
        // can be overridden in subclass
    }

    @Override public Parcelable superOnSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override public void superOnRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }
}
