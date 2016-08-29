package ru.android.shiz.ra.menu;

import java.util.List;

import ru.android.shiz.ra.base.view.AuthView;
import ru.android.shiz.ra.model.account.Account;
import ru.android.shiz.ra.model.stream.Label;

/**
 * Created by kassava on 24.08.2016.
 */
public interface MenuView extends AuthView<List<Label>> {

    public void setAccount(Account account);

    public void decrementUnreadCount(String label);
}
