package ru.android.shiz.ra.menu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ru.android.shiz.ra.IntentStarter;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.RaApp;
import ru.android.shiz.ra.base.view.AuthRefreshRecyclerFragment;
import ru.android.shiz.ra.base.view.ListAdapter;
import ru.android.shiz.ra.base.view.viewstate.AuthViewState;
import ru.android.shiz.ra.dagger.NavigationModule;
import ru.android.shiz.ra.model.account.Account;
import ru.android.shiz.ra.model.stream.Label;
import ru.android.shiz.ra.statistics.StatisticsDialog;

/**
 * Created by kassava on 24.08.2016.
 */
public class MenuFragment extends AuthRefreshRecyclerFragment<List<Label>, MenuView, MenuPresenter>
        implements MenuView, MenuAdapter.LabelClickListener {

    @BindView(R.id.estream) TextView stream;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.profilePic) ImageView profilePic;

    @Inject
    EventBus eventBus;
    @Inject
    IntentStarter intentStarter;

    private MenuComponent menuComponent;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_menu;
    }

    @Override
    public AuthViewState<List<Label>, MenuView> createViewState() {
        return new MenuViewState();
    }

    @Override
    protected ListAdapter<List<Label>> createAdapter() {
        return new MenuAdapter(getActivity(), this);
    }

    @Override
    public void setAccount(Account account) {

        MenuViewState vs = (MenuViewState) viewState;
        vs.setAccount(account);

        stream.setText(account.getEmail());
        name.setText(account.getName());
        profilePic.setImageResource(account.getImageRes());

        stream.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        profilePic.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAuthenticationRequired() {
        super.showAuthenticationRequired();

        stream.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        profilePic.setVisibility(View.GONE);
    }

    @Override
    public MenuPresenter createPresenter() {
        return menuComponent.presenter();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadLabels(pullToRefresh);
    }

    @Override
    protected void injectDependencies() {
        menuComponent = DaggerMenuComponent.builder()
                .streamAppComponent(RaApp.getStreamComponents())
                .navigationModule(new NavigationModule())
                .build();
        menuComponent.inject(this);
    }

    @Override public void onLabelClicked(Label label) {
        intentStarter.showStreamsOfLabel(getActivity(), label);
    }

    @Override public void onStatisticsClicked() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(new StatisticsDialog(), null)
                .commit();
    }

    @Override public void decrementUnreadCount(String label) {

        for (Label l : adapter.getItems()) {
            if (l.getName().equals(label)) {
                l.decrementUnreadCount();
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }
}
