package ru.android.shiz.ra.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.android.shiz.ra.model.contact.ContactsManager;

/**
 * Created by kassava on 22.08.2016.
 */
@Module
public class ContactsModule {

    @Singleton @Provides
    public ContactsManager contactsManager() {
        return new ContactsManager();
    }
}
