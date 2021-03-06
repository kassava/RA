package ru.android.shiz.ra.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.android.shiz.ra.broadcastpreview.BroadcastPreviewPresenter;
import ru.android.shiz.ra.broadcastpreview.BroadcastPreviewView;
import ru.android.shiz.ra.broadcasts.BroadcastsPresenter;
import ru.android.shiz.ra.broadcastdetails.BroadcastDetailsPresenter;

/**
 * Created by kassava on 09.05.2016.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    public BroadcastsPresenter broadcastsPresenter();

    public BroadcastDetailsPresenter broadcastDetailsPresenter();

    public BroadcastPreviewPresenter broadcastPreviewPresenter();
}
