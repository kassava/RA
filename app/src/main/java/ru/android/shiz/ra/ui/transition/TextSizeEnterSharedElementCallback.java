package ru.android.shiz.ra.ui.transition;

import android.annotation.TargetApi;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import ru.android.shiz.ra.R;

/**
 * Created by kassava on 25.08.2016.
 */
@TargetApi(21)
public class TextSizeEnterSharedElementCallback extends SharedElementCallback {
    private static final String TAG = "EnterSharedElementCallback";

    private final float mStartTextSize;
    private final float mEndTextSize;

    public TextSizeEnterSharedElementCallback(Context context) {
        Resources res = context.getResources();
        mStartTextSize = res.getDimensionPixelSize(R.dimen.list_stream_item_subject_text_size);
        mEndTextSize = res.getDimensionPixelSize(R.dimen.details_subject_text_size);
    }

    @Override
    public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements,
                                     List<View> sharedElementSnapshots) {

        TextView textView = findTextView(sharedElements);

        // Setup the TextView's start values.
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mStartTextSize);
    }

    @TargetApi(21) private TextView findTextView(List<View> sharedElements) {
        for (View v : sharedElements) {
            if (v.getTransitionName().equals(v.getResources().getString(R.string.shared_stream_subject))) {
                return (TextView) v;
            }
        }
        return null;
    }

    @Override
    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements,
                                   List<View> sharedElementSnapshots) {

        TextView textView = findTextView(sharedElements);

        // Setup the TextView's end values.
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEndTextSize);

    }
}
