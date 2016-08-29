package ru.android.shiz.ra.login;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ru.android.shiz.ra.model.account.Account;
import ru.android.shiz.ra.model.account.AccountManager;
import ru.android.shiz.ra.model.account.AuthCredentials;
import ru.android.shiz.ra.model.event.LoginSuccessfulEvent;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kassava on 22.08.2016.
 */
public class LoginPresenter extends MvpBasePresenter<LoginView> {

    private AccountManager accountManager;
    private Subscriber<Account> subscriber;
    private EventBus eventBus;

    @Inject
    public LoginPresenter(AccountManager accountManager, EventBus eventBus) {
        this.accountManager = accountManager;
        this.eventBus = eventBus;
    }

    public void doLogin(AuthCredentials credentials) {

        if (isViewAttached()) {
            getView().showLoading();
        }

        // Kind of "callback"
        cancelSubscription();
        subscriber = new Subscriber<Account>() {
            @Override
            public void onCompleted() {
                if (isViewAttached()) {
                    getView().loginSuccessful();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showError();
                }
            }

            @Override
            public void onNext(Account account) {
                eventBus.post(new LoginSuccessfulEvent(account));
            }
        };

        // do the login
        accountManager.doLogin(credentials)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * Cancels any previous callback
     */
    private void cancelSubscription() {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            cancelSubscription();
        }
    }

    @Override public void attachView(LoginView view) {
        super.attachView(view);
    }
}
