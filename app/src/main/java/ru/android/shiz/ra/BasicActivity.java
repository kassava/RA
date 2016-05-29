package ru.android.shiz.ra;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import flow.Flow;
import ru.android.shiz.ra.flow.RAAppDispatcher;
import ru.android.shiz.ra.flow.RAAppKeyParceler;
import ru.android.shiz.ra.streams.StreamsScreen;

/**
 * Created by kassava on 24.04.2016.
 */
public class BasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
    }

    @Override
    protected void attachBaseContext(Context baseContextWrapper) {
        baseContextWrapper = Flow.configure(baseContextWrapper, this)
                .dispatcher(new RAAppDispatcher(this))
                .defaultKey(new StreamsScreen())
                .keyParceler(new RAAppKeyParceler())
                .install();
        super.attachBaseContext(baseContextWrapper);
    }

    @Override
    public void onBackPressed() {
        if (!Flow.get(this).goBack()) {
            super.onBackPressed();
        }
    }
}
