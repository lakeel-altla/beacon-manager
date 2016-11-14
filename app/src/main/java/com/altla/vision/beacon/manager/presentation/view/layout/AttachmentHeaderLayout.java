package com.altla.vision.beacon.manager.presentation.view.layout;

import com.altla.vision.beacon.manager.R;

import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;

public final class AttachmentHeaderLayout {

    @BindView(R.id.imageView_add)
    ImageView mImageView;

    private View.OnClickListener mOnClickListener;

    public AttachmentHeaderLayout(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setClickable(boolean clickable) {
        if (!clickable) {
            mImageView.setVisibility(View.GONE);
        } else {
            mImageView.setOnClickListener(view -> mOnClickListener.onClick(view));
        }
    }
}
