package ru.android.shiz.ra.broadcastpreview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.hannesdorfmann.mosby.mvp.layout.MvpFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.shiz.ra.R;
import ru.android.shiz.ra.RaApp;

/**
 * Created by kassava on 12.09.16.
 */
public class BroadcastPreviewLayout extends MvpFrameLayout<BroadcastPreviewView, BroadcastPreviewPresenter>
        implements BroadcastPreviewView {

    private final String LOG_TAG = BroadcastPreviewLayout.class.getSimpleName();

    private Context context;

    @BindView(R.id.texture) AutoFitTextureView textureView;

    public BroadcastPreviewLayout(Context ctx, AttributeSet attributeSet) {
        super(ctx, attributeSet);

        this.context = ctx;

    }

    @Override
    public BroadcastPreviewPresenter createPresenter() {
        return RaApp.getComponent().broadcastPreviewPresenter();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }


}
