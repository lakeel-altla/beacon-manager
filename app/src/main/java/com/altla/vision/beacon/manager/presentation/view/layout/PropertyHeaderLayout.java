package com.altla.vision.beacon.manager.presentation.view.layout;

import com.altla.vision.beacon.manager.R;

import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;

public final class PropertyHeaderLayout {

    @BindView(R.id.imageView_add)
    ImageView imageView;

    private View.OnClickListener onClickListener;

    public PropertyHeaderLayout(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setClickable(boolean isClickable) {
        if (!isClickable) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setOnClickListener(view -> onClickListener.onClick(view));
        }
    }
}
