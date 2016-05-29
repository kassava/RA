package ru.android.shiz.ra.flow;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import flow.Dispatcher;
import flow.Traversal;
import flow.TraversalCallback;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.streamdetails.StreamDetailsScreen;
import ru.android.shiz.ra.streams.StreamsScreen;

/**
 * To tell Flow how to navigate in our app we have to define a Dispatcher.
 * The Dispatcher is responsible to “dispatch” changes on Flow’s navigation (history) stack.
 *
 * Created by kassava on 24.04.2016.
 */
final public class RAAppDispatcher implements Dispatcher {

    private final String LOG_TAG = RAAppDispatcher.class.getSimpleName();
    private final Activity activity;

    public RAAppDispatcher(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void dispatch(@NonNull Traversal traversal, @NonNull TraversalCallback callback) {
        Log.d(LOG_TAG, "dispatching " + traversal);
        Object destination = traversal.destination.top(); // destination key

        // Update container: remove oldView, insert newView
        ViewGroup frame = (ViewGroup) activity.findViewById(R.id.container);

        // Remove current screen from container
        if (traversal.origin != null) {
            if (frame.getChildCount() > 0) {
                traversal.getState(traversal.origin.top()).save(frame.getChildAt(0));
                frame.removeAllViews();
            }
        }

        @LayoutRes final int layout;
        if (destination instanceof StreamsScreen) {
            layout = R.layout.screen_streams;
        } else {
            if (destination instanceof StreamDetailsScreen) {
                layout = R.layout.screen_stream_details;
            } else {
                throw new AssertionError("Unrecognized screen " + destination);
            }
        }

        View incomingView = LayoutInflater.from(traversal.createContext(destination, activity))
                .inflate(layout, frame, false);

        // Add new screen
        frame.addView(incomingView);

        // Restore state before adding view (i.e. caused by onBackPressed)
        traversal.getState(traversal.destination.top()).restore(incomingView);

        callback.onTraversalCompleted(); // Tell Flow that we are done
    }
}
