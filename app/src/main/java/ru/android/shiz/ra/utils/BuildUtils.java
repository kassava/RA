package ru.android.shiz.ra.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

import ru.android.shiz.ra.R;

/**
 * Little helper class to detect current installed  sdk version
 *
 * Created by kassava on 25.08.2016.
 */
public class BuildUtils {

    public static boolean isMinApi21() {
        return Build.VERSION.SDK_INT >= 21;
    }

    @TargetApi(21) public static Drawable getBackArrowDrawable(Context context) {

        if (isMinApi21()) {
            return context.getResources()
                    .getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha, context.getTheme());
        } else {
            return context.getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        }
    }
}
