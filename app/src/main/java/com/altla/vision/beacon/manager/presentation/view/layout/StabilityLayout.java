package com.altla.vision.beacon.manager.presentation.view.layout;

import com.altla.vision.beacon.manager.R;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

public final class StabilityLayout {

    @BindView(R.id.layout)
    LinearLayout linearLayout;

    @BindView(R.id.textView_title)
    public TextView title;

    @BindView(R.id.textView_value)
    public TextView value;

    private View.OnClickListener onClickListener;

    private boolean clickable = true;

    public StabilityLayout(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    @OnClick(R.id.layout)
    public void onClick(View view) {
        if (clickable) {
            onClickListener.onClick(view);
        }
    }
}
