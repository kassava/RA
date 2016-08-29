package ru.android.shiz.ra.search;

import android.os.Bundle;

import ru.android.shiz.ra.R;
import ru.android.shiz.ra.base.view.BaseActivity;

/**
 * Created by kassava on 25.08.2016.
 */
public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new SearchFragment())
                    .commit();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
