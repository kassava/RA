package ru.android.shiz.ra.profile;

import java.util.List;

import javax.inject.Inject;

import ru.android.shiz.ra.base.presenter.BaseRxLcePresenter;
import ru.android.shiz.ra.model.contact.ContactsManager;
import ru.android.shiz.ra.model.contact.Person;
import ru.android.shiz.ra.model.contact.ProfileScreen;

/**
 * Created by kassava on 24.08.2016.
 */
public class ProfilePresenter extends BaseRxLcePresenter<ProfileView, List<ProfileScreen>> {

    private ContactsManager contactsManager;

    @Inject
    public ProfilePresenter(ContactsManager contactsManager) {
        this.contactsManager = contactsManager;
    }

    public void loadScreens(Person person) {
        subscribe(contactsManager.getProfileScreens(person), false);
    }
}
