package com.altla.vision.beacon.manager.presentation.view.layout;

import com.altla.vision.beacon.manager.R;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

public final class DescriptionLayout {

    @BindView(R.id.textView_title)
    public TextView mTitle;

    @BindView(R.id.textView_value)
    public TextView mValue;

    private boolean mClickable = true;

    private View.OnClickListener mOnClickListener;

    public DescriptionLayout(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setClickable(boolean clickable) {
        mClickable = clickable;
    }

    @OnClick(R.id.layout)
    public void onClick(View view) {
        if (mClickable) {
            mOnClickListener.onClick(view);
        }
    }
}
