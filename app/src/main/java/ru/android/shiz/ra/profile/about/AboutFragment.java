package ru.android.shiz.ra.profile.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.annotation.Arg;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.base.view.BaseFragment;
import ru.android.shiz.ra.model.contact.Person;

/**
 * Created by kassava on 24.08.2016.
 */
public class AboutFragment extends BaseFragment {

    @Arg Person person;

    @BindView(R.id.email) TextView estream;
    @BindView(R.id.birthday) TextView birthday;
    @BindView(R.id.bio) TextView bio;

    @Override protected int getLayoutRes() {
        return R.layout.fragment_about;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());

        if (!TextUtils.isEmpty(person.getEstream())) {
            estream.setText(person.getEstream());
        }

        if (person.getBirthday() != null) {
            birthday.setText(sdf.format(person.getBirthday()));
        }

        if (person.getBioRes() != 0) {
            bio.setText(person.getBioRes());
        }
    }
}
