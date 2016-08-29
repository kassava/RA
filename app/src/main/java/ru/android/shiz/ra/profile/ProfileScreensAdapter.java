package ru.android.shiz.ra.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import ru.android.shiz.ra.model.contact.Person;
import ru.android.shiz.ra.model.contact.ProfileScreen;
import ru.android.shiz.ra.profile.about.AboutFragmentBuilder;
import ru.android.shiz.ra.profile.streams.ProfileStreamsFragmentBuilder;

/**
 * Created by kassava on 24.08.2016.
 */
public class ProfileScreensAdapter extends FragmentPagerAdapter {

    private List<ProfileScreen> screens;
    private Person person;

    public ProfileScreensAdapter(FragmentManager fm, Person person) {
        super(fm);
        this.person = person;
    }

    public List<ProfileScreen> getScreens() {
        return screens;
    }

    public void setScreens(List<ProfileScreen> screens) {
        this.screens = screens;
    }

    @Override
    public Fragment getItem(int position) {

        ProfileScreen screen = screens.get(position);
        if (screen.getType() == ProfileScreen.TYPE_STREAMS) {
            return new ProfileStreamsFragmentBuilder(person).build();
        }
        if (screen.getType() == ProfileScreen.TYPE_ABOUT) {
            return new AboutFragmentBuilder(person).build();
        }

        throw new RuntimeException("Unknown Profile Screen (no fragment associated with this type");
    }

    @Override public int getCount() {

        return screens == null ? 0 : screens.size();
    }

    @Override public CharSequence getPageTitle(int position) {
        return screens.get(position).getName();
    }
}
