package com.altla.vision.beacon.manager.presentation.view.layout;

import com.altla.vision.beacon.manager.R;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

public final class PlaceIdLayout {

    @BindView(R.id.layout)
    LinearLayout mLinearLayout;

    @BindView(R.id.textView_title)
    public TextView mTitle;

    @BindView(R.id.textView_value)
    public TextView mValue;

    private View.OnClickListener mOnClickListener;

    private boolean mClickable = true;

    public PlaceIdLayout(View.OnClickListener onClickListener) {
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
