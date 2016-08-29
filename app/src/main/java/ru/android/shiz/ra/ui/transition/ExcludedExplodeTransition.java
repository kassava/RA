package ru.android.shiz.ra.ui.transition;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Explode;

import ru.android.shiz.ra.R;

/**
 * Created by kassava on 25.08.2016.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ExcludedExplodeTransition extends Explode {

    public ExcludedExplodeTransition() {
        excludeTarget(R.id.toolbar, true);
        excludeTarget(android.R.id.statusBarBackground, true);
        excludeTarget(android.R.id.navigationBarBackground, true);
    }
}
