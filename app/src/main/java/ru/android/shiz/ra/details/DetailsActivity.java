package ru.android.shiz.ra.details;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.base.view.BaseActivity;
import ru.android.shiz.ra.model.contact.Person;
import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.utils.BuildUtils;

/**
 * Created by kassava on 22.08.2016.
 */
public class DetailsActivity extends BaseActivity {

    public static final String KEY_STREAM = "com.hannesdorfmann.mosby.MosbyActivity.MAIL";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @TargetApi(21) @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_details);

        // Activity Transitions
        if (BuildUtils.isMinApi21()) {
            postponeEnterTransition();
        }

        toolbar.setNavigationIcon(BuildUtils.getBackArrowDrawable(this));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 21) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });

        if (savedInstanceState == null) {
            Stream stream = getIntent().getParcelableExtra(KEY_STREAM);
            Person sender = stream.getSender();

            DetailsFragment fragment =
                    new DetailsFragmentBuilder(stream.getDate().getTime(), sender.getEstream(), sender.getName(),
                            sender.getImageRes(), stream.isStarred(), stream.getId(), stream.getSubject()).build();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }



}
