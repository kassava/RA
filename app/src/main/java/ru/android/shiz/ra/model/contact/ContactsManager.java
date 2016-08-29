package ru.android.shiz.ra.model.contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created by kassava on 24.08.2016.
 */
public class ContactsManager {

    public Observable<List<ProfileScreen>> getProfileScreens(Person person) {

        // TODO throw error from time to time
        return Observable.defer(new Func0<Observable<List<ProfileScreen>>>() {
            @Override
            public Observable<List<ProfileScreen>> call() {

                List<ProfileScreen> screens = new ArrayList<ProfileScreen>();
                screens.add(new ProfileScreen(ProfileScreen.TYPE_STREAMS, "Streams"));
                screens.add(new ProfileScreen(ProfileScreen.TYPE_ABOUT, "About"));

                return Observable.just(screens);
            }
        }).delay(2, TimeUnit.SECONDS);
    }
}
