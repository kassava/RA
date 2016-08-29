package ru.android.shiz.ra;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ru.android.shiz.ra.details.DetailsActivity;
import ru.android.shiz.ra.login.LoginActivity;
import ru.android.shiz.ra.model.contact.Person;
import ru.android.shiz.ra.model.stream.Label;
import ru.android.shiz.ra.model.stream.Stream;
import ru.android.shiz.ra.model.stream.service.SendStreamService;
import ru.android.shiz.ra.profile.ProfileActivity;
import ru.android.shiz.ra.search.SearchActivity;
import ru.android.shiz.ra.write.WriteActivity;

/**
 * A simple helper class that helps to create and launch Intents. It checks if we our device is a
 * phone or a tablet app.
 *
 * Created by kassava on 25.08.2016.
 */
// TODO make it injectable with dagger
public class IntentStarter {

    private boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.tablet);
    }

    public void showStreamDetails(Context context, Stream stream, Bundle activityTransitionBundle) {

        Intent i = null;
        if (isTablet(context)) {
            i = new Intent(context, MainActivity.class);
            i.putExtra(MainActivity.KEY_SHOW_ACTION, MainActivity.KEY_SHOW_ACTION_STREAM_DETAILS);
            i.putExtra(MainActivity.KEY_DATA_STREAM_DETAILS, stream);
        } else {
            i = getShowStreamInNewActivityIntent(context, stream);
        }

        context.startActivity(i, activityTransitionBundle);
    }

    public Intent getShowStreamInNewActivityIntent(Context context, Stream stream) {
        Intent i = new Intent(context, DetailsActivity.class);
        i.putExtra(DetailsActivity.KEY_STREAM, stream);
        return i;
    }

    public void showStreamsOfLabel(Context context, Label label) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra(MainActivity.KEY_SHOW_ACTION, MainActivity.KEY_SHOW_ACTION_STREAMS_OF_LABEL);
        i.putExtra(MainActivity.KEY_DATA_STREAMS_OF_LABEL, label);

        context.startActivity(i);
    }

    public void showWriteStream(Context context, Stream replayTo, Bundle activityTransitionBundle) {

        Intent i = new Intent(context, WriteActivity.class);

        if (replayTo != null) {
            i.putExtra(WriteActivity.KEY_REPLAY_STREAM, replayTo);
        }

        context.startActivity(i, activityTransitionBundle);
    }

    public void showAuthentication(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public void sendStreamViaService(Context context, Stream stream) {
        Intent i = new Intent(context, SendStreamService.class);
        i.putExtra(SendStreamService.KEY_STREAM, stream);
        context.startService(i);
    }

    public void showSearch(Activity context) {
        Intent i = new Intent(context, SearchActivity.class);
        context.startActivity(i);
        context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void showProfile(Context context, Person person) {
        Intent i = new Intent(context, ProfileActivity.class);
        i.putExtra(ProfileActivity.KEY_PERSON, person);
        context.startActivity(i);
    }

}
