package ru.android.shiz.ra.dagger;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.android.shiz.ra.model.account.AccountManager;
import ru.android.shiz.ra.model.account.DefaultAccountManager;
import ru.android.shiz.ra.model.stream.RandomStreamGenerator;
import ru.android.shiz.ra.model.stream.StreamGenerator;
import ru.android.shiz.ra.model.stream.StreamProvider;

/**
 * Created by kassava on 22.08.2016.
 */
@Module
public class StreamModule {

    // Singletons
    private static StreamGenerator generator = new RandomStreamGenerator();
    private static AccountManager accountManager = new DefaultAccountManager();
    private static StreamProvider streamProvider = new StreamProvider(accountManager, generator);

    @Singleton @Provides
    public AccountManager providesAccountManager() {
        return accountManager;
    }

    @Singleton @Provides
    public EventBus providesEventBus() {
        return EventBus.getDefault();
    }

    @Singleton @Provides
    public StreamProvider providesStreamProvider(AccountManager manager, StreamGenerator generator) {
        return streamProvider;
    }

    @Singleton @Provides
    public StreamGenerator providesStreamGenerator() {
        return generator;
    }
}
