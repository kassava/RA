package ru.android.shiz.ra.menu;

import android.os.Parcel;

import java.util.List;

import ru.android.shiz.ra.base.view.viewstate.AuthCastedArrayListViewState;
import ru.android.shiz.ra.model.account.Account;
import ru.android.shiz.ra.model.stream.Label;

/**
 * Created by kassava on 24.08.2016.
 */
@SuppressWarnings("ParcelCreator")
public class MenuViewState extends AuthCastedArrayListViewState<List<Label>, MenuView> {

    private Account account;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(account, flags);
    }

    @Override
    protected void readFromParcel(Parcel source) {
        super.readFromParcel(source);
        account = source.readParcelable(Account.class.getClassLoader());
    }

    @Override
    public void apply(MenuView view, boolean retained) {
        super.apply(view, retained);

        if (account != null) {
            view.setAccount(account);
        } else {
            view.showAuthenticationRequired();
        }
    }

    @Override
    public void setShowingAuthenticationRequired() {
        super.setShowingAuthenticationRequired();
        account = null;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
