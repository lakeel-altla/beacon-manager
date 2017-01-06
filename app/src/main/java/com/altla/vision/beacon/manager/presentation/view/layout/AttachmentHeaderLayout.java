package com.altla.vision.beacon.manager.presentation.view.layout;

import com.altla.vision.beacon.manager.R;

import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;

public final class AttachmentHeaderLayout {

    @BindView(R.id.imageView_add)
    ImageView imageView;

    private View.OnClickListener mOnClickListener;

    public AttachmentHeaderLayout(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setClickable(boolean clickable) {
        if (!clickable) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setOnClickListener(view -> mOnClickListener.onClick(view));
        }
    }
}
