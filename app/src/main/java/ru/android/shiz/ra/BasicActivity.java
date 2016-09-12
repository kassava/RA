package ru.android.shiz.ra;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import flow.Flow;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import ru.android.shiz.ra.flow.RaAppDispatcher;
import ru.android.shiz.ra.flow.RaAppKeyParceler;
import ru.android.shiz.ra.mortar.DetailsMortarPresenter;
import ru.android.shiz.ra.mortar.MortarPresenter;
import ru.android.shiz.ra.broadcasts.BroadcastsScreen;

import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;

/**
 * Created by kassava on 24.04.2016.
 */
public class BasicActivity extends AppCompatActivity {

    private MortarScope activityScope;

    @Override
    public Object getSystemService(String name) {
        MortarScope activityScope = findChild(getApplicationContext(), getScopeName());

        if (activityScope == null) {
            activityScope = buildChild(getApplicationContext())
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(MortarPresenter.class.getName(), new MortarPresenter())
                    .withService(DetailsMortarPresenter.class.getName(), new DetailsMortarPresenter())
                    .build(getScopeName());
        }
        return activityScope.hasService(name) ? activityScope.getService(name)
                : super.getSystemService(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);

        setContentView(R.layout.activity_basic);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }

    @Override protected void onDestroy() {
        if (isFinishing()) {
            MortarScope activityScope = findChild(getApplicationContext(), getScopeName());
            if (activityScope != null) activityScope.destroy();
        }

        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context baseContextWrapper) {
        baseContextWrapper = Flow.configure(baseContextWrapper, this)
                .dispatcher(new RaAppDispatcher(this))
                .defaultKey(new BroadcastsScreen())
                .keyParceler(new RaAppKeyParceler())
                .install();
        super.attachBaseContext(baseContextWrapper);
    }

    @Override
    public void onBackPressed() {
        if (!Flow.get(this).goBack()) {
            super.onBackPressed();
        }
    }

    private String getScopeName() {
        return getClass().getName() + "-task-" + getTaskId();
    }
}
