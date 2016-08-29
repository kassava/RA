package ru.android.shiz.ra.ui.transition;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Explode;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;

import ru.android.shiz.ra.R;

/**
 * Created by kassava on 25.08.2016.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ExplodeFadeExitTransition extends Explode {

    private View senderNameView;
    private View senderMailView;
    private View separatorLine;

    public ExplodeFadeExitTransition(View senderNameView, View senderMailView, View separatorLine) {
        this.senderMailView = senderMailView;
        this.senderNameView = senderNameView;
        this.separatorLine = separatorLine;
        excludeTarget(R.id.toolbar, true);
        excludeTarget(android.R.id.statusBarBackground, true);
        excludeTarget(android.R.id.navigationBarBackground, true);
        excludeTarget(R.id.senderName, true);
        excludeTarget(R.id.senderMail, true);
        excludeTarget(R.id.separatorLine, true);
    }

    @Override public Animator createAnimator(final ViewGroup sceneRoot, TransitionValues startValues,
                                             TransitionValues endValues) {

        senderNameView.setVisibility(View.INVISIBLE);
        senderMailView.setVisibility(View.INVISIBLE);
        separatorLine.setVisibility(View.INVISIBLE);
        return super.createAnimator(sceneRoot, startValues, endValues);
    }
}
