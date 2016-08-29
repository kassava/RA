package ru.android.shiz.ra.ui.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.os.Build;

/**
 * Created by kassava on 25.08.2016.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class PauseableAnimator extends Animator {

    private final Animator mWrappedAnimator;

    public PauseableAnimator(Animator wrappedAnimator) {
        mWrappedAnimator = wrappedAnimator;
    }

    @Override public long getStartDelay() {
        return mWrappedAnimator.getStartDelay();
    }

    @Override public void setStartDelay(long startDelay) {
        mWrappedAnimator.setStartDelay(startDelay);
    }

    @Override public Animator setDuration(long duration) {
        mWrappedAnimator.setDuration(duration);
        return this;
    }

    @Override public long getDuration() {
        return mWrappedAnimator.getDuration();
    }

    @Override public void setInterpolator(TimeInterpolator value) {
        mWrappedAnimator.setInterpolator(value);
    }

    @Override public boolean isRunning() {
        return mWrappedAnimator.isRunning();
    }

    @Override public void start() {
        mWrappedAnimator.start();
    }

    @Override public void cancel() {
        mWrappedAnimator.cancel();
    }

    @Override public void pause() {
        if (!isRevealAnimator()) {
            mWrappedAnimator.pause();
        } else {
        }
    }

    @Override public void resume() {
        if (!isRevealAnimator()) {
            mWrappedAnimator.resume();
        } else {
        }
    }

    @Override public void addListener(Animator.AnimatorListener listener) {
        mWrappedAnimator.addListener(listener);
    }

    @Override public void removeAllListeners() {
        mWrappedAnimator.removeAllListeners();
    }

    @Override public void removeListener(AnimatorListener listener) {
        mWrappedAnimator.removeListener(listener);
    }

    private boolean isRevealAnimator() {
        return mWrappedAnimator.getClass().getName().contains("RevealAnimator");
    }
}
