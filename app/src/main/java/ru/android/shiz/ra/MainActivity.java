package ru.android.shiz.ra;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import ru.android.shiz.ra.base.view.BaseActivity;
import ru.android.shiz.ra.details.DetailsFragment;
import ru.android.shiz.ra.details.DetailsFragmentBuilder;
import ru.android.shiz.ra.model.contact.Person;
import ru.android.shiz.ra.model.stream.Label;
import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.model.stream.StreamProvider;
import ru.android.shiz.ra.streams.StreamsFragment;
import ru.android.shiz.ra.streams.StreamsFragmentBuilder;

/**
 * Created by kassava on 24.04.2016.
 */
public class MainActivity extends BaseActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String KEY_SHOW_ACTION =
            "ru.android.shiz.ra.MainActivity.SHOW_ACTION";
    public static final String KEY_SHOW_ACTION_STREAM_DETAILS =
            "ru.android.shiz.ra.MainActivity.SHOW_ACTION_STREAM_DETAILS";
    public static final String KEY_DATA_STREAM_DETAILS =
            "ru.android.shiz.ra.MainActivity.STREAM";
    public static final String KEY_SHOW_ACTION_STREAMS_OF_LABEL =
            "ru.android.shiz.ra.MainActivity.SHOW_ACTION_STREAMS_OF_LABEL";
    public static final String KEY_DATA_STREAMS_OF_LABEL =
            "ru.android.shiz.ra.MainActivity.LABEL";

    public static final String FRAGMENT_TAG_DETAILS = "detailsFragmentTag";
    public static final String FRAGMENT_TAG_LABEL = "labelFragmentTag";

    Fragment detailsFragment;
    StreamsFragment labelFragment;

    @State String toolbarTitle;

    @Inject IntentStarter intentStarter;

    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.leftPane) ViewGroup leftPane;
    @Nullable
    @BindView(R.id.rightPane) ViewGroup rightPane;
    // contains leftPane + rightPane
    @Nullable @BindView(R.id.paneContainer) ViewGroup paneContainer;

    ActionBarDrawerToggle drawerToggle;
    private MainActivityComponent mainActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar

        if (toolbar == null) {
            Log.d(LOG_TAG, "toolbar null: " + R.menu.search_menu);
        }

        toolbar.inflateMenu(R.menu.search_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.search) {
                    intentStarter.showSearch(MainActivity.this);
                    return true;
                }
                return false;
            }
        });
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        if (toolbarTitle != null) {
            toolbar.setTitle(toolbarTitle);
        }

        // Check for previous fragments
        detailsFragment = findDetailsFragment();
        labelFragment =
                (StreamsFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_LABEL);

        if (detailsFragment != null) {
            // details fragment available, so make it visible
            rightPane.setVisibility(View.VISIBLE);
        }

        if (paneContainer != null) {
            // Enable animation
            LayoutTransition transition = new LayoutTransition();
            transition.enableTransitionType(LayoutTransition.CHANGING);
            paneContainer.setLayoutTransition(transition);
        }

        if (labelFragment == null) {
            // First app start, so start with this
            showStreams(StreamProvider.INBOX_LABEL, true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String showAction = intent.getStringExtra(KEY_SHOW_ACTION);

        if (KEY_SHOW_ACTION_STREAM_DETAILS.equals(showAction)) {
            Stream stream = intent.getParcelableExtra(KEY_DATA_STREAM_DETAILS);
            showStream(stream);
        } else if (KEY_SHOW_ACTION_STREAMS_OF_LABEL.equals(showAction)) {
            Label label = intent.getParcelableExtra(KEY_DATA_STREAMS_OF_LABEL);
            showStreams(label, true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void showStreams(Label label, boolean removeDetailsFragment) {
        toolbarTitle = label.getName();
        toolbar.setTitle(toolbarTitle);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.leftPane, new StreamsFragmentBuilder(label).build(), FRAGMENT_TAG_LABEL)
                .commit();

        if (removeDetailsFragment) {
            removeDetailsFragment();
        }
    }

    private void showStream(Stream stream) {

        rightPane.setVisibility(View.VISIBLE);
        Person sender = stream.getSender();
        DetailsFragment fragment =
                new DetailsFragmentBuilder(stream.getDate().getTime(), sender.getEstream(), sender.getName(),
                        sender.getImageRes(), stream.isStarred(), stream.getId(), stream.getSubject()).build();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rightPane, fragment, FRAGMENT_TAG_DETAILS)
                .commit();
    }

    private Fragment findDetailsFragment() {
        return getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_DETAILS);
    }

    /**
     * @return true if a fragment has been removed, otherwise false
     */
    private boolean removeDetailsFragment() {
        Fragment detailsFragment = findDetailsFragment();
        if (detailsFragment != null) {
            rightPane.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().remove(detailsFragment).commit();
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {

        // TODO make this easier to read
        if (!removeDetailsFragment()) {
            super.onBackPressed();
        }
    }

    protected void injectDependencies() {
        mainActivityComponent = DaggerMainActivityComponent.create();
        mainActivityComponent.inject(this);
    }
}
