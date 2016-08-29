package ru.android.shiz.ra.write;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

import java.util.Date;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.shiz.ra.IntentStarter;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.RaApp;
import ru.android.shiz.ra.base.view.BaseViewStateActivity;
import ru.android.shiz.ra.dagger.NavigationModule;
import ru.android.shiz.ra.model.contact.Person;
import ru.android.shiz.ra.model.stream.Label;
import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.utils.BuildUtils;

/**
 * Created by kassava on 25.08.2016.
 */
@TargetApi(21) public class WriteActivity extends BaseViewStateActivity<WriteView, WritePresenter>
        implements WriteView {

    public static final String KEY_REPLAY_STREAM =
            "ru.android.shiz.ra.write.REPLAY_STREAM";

    @BindView(R.id.loadingOverlay) View loadingOverlay;
    @BindView(R.id.authOverlay) View authOverlay;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.message) EditText message;
    @BindView(R.id.subject) EditText subject;
    @BindView(R.id.receiver) EditText receiver;

    @Inject
    IntentStarter intentStarter;

    private WriteComponent writeComponent;

    private Pattern emailPattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        if (BuildUtils.isMinApi21()) {
            getWindow().getEnterTransition()
                    .excludeTarget(R.id.toolbar, true)
                    .excludeTarget(android.R.id.statusBarBackground, true)
                    .excludeTarget(android.R.id.navigationBarBackground, true);
        }

        toolbar.setNavigationIcon(getBackArrowDrawable());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (BuildUtils.isMinApi21()) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });

        Stream replayStream = getIntent().getParcelableExtra(KEY_REPLAY_STREAM);
        if (replayStream != null) {
            if (TextUtils.isEmpty(receiver.getText().toString())) {
                receiver.setText(replayStream.getSender().getEstream());
            }

            if (TextUtils.isEmpty(subject.getText().toString())) {
                subject.setText("RE: " + replayStream.getSubject());
            }
        }
    }

    @TargetApi(21) private Drawable getBackArrowDrawable() {

        if (BuildUtils.isMinApi21()) {
            return getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha, getTheme());
        } else {
            return getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        }
    }

    @Override
    public RestorableViewState createViewState() {
        return new WriteViewState();
    }

    @Override
    public WritePresenter createPresenter() {
        return writeComponent.presenter();
    }

    @Override
    public void onNewViewStateInstance() {
        showForm();
    }

    @Override
    public WriteViewState getViewState() {
        return (WriteViewState) super.getViewState();
    }

    @Override
    public void showForm() {
        getViewState().setStateShowForm();
        loadingOverlay.setVisibility(View.GONE);
        authOverlay.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        getViewState().setStateShowLoading();
        authOverlay.setVisibility(View.GONE);
        loadingOverlay.setVisibility(View.VISIBLE);
        if (!restoringViewState) {
            loadingOverlay.setAlpha(0f);
            loadingOverlay.animate().alpha(1f).setDuration(200).start();
        }
    }

    @Override
    public void showError(Throwable e) {
        Toast.makeText(this, R.string.error_sending_stream, Toast.LENGTH_SHORT).show();
        showForm();
    }

    @Override
    public void showAuthenticationRequired() {
        getViewState().setStateAuthenticationRequired();
        loadingOverlay.setVisibility(View.GONE);
        authOverlay.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.authView) public void onAuthViewClicked() {
        intentStarter.showAuthentication(this);
    }

    @OnClick(R.id.send) public void onSendClicked() {

        String email = receiver.getText().toString();
        if (TextUtils.isEmpty(email) || !emailPattern.matcher(email).matches()) {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            receiver.startAnimation(shake);
            return;
        }

        String sub = subject.getText().toString();
        if (TextUtils.isEmpty(sub)) {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            subject.startAnimation(shake);
            return;
        }

        Person receiver = null;
        if (email.equals(Person.BARNEY.getEstream())) {
            receiver = Person.BARNEY;
        } else if (email.equals(Person.LILY.getEstream())) {
            receiver = Person.LILY;
        } else if (email.equals(Person.MARSHALL.getEstream())) {
            receiver = Person.MARSHALL;
        } else if (email.equals(Person.ROBIN.getEstream())) {
            receiver = Person.ROBIN;
        } else {
            String name = email.split("@")[0];
            receiver = new Person(23, name, email, R.drawable.unknown, null, 0);
        }

        String text = message.getText().toString();

        Stream stream = new Stream().date(new Date())
                .label(Label.SENT)
                .sender(Person.TED)
                .receiver(receiver)
                .subject(sub)
                .text(text);

        presenter.writeMail(getApplicationContext(), stream);
    }

    @Override
    public void finishBecauseSuccessful() {
        if (BuildUtils.isMinApi21()) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    protected void injectDependencies() {
        writeComponent = DaggerWriteComponent.builder()
                .streamAppComponent(RaApp.getStreamComponents())
                .navigationModule(new NavigationModule())
                .build();

        writeComponent.inject(this);
    }
}
