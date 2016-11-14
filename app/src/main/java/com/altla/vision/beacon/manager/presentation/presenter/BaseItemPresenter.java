package com.altla.vision.beacon.manager.presentation.presenter;

import com.altla.vision.beacon.manager.presentation.view.ItemView;
import com.altla.vision.beacon.manager.presentation.presenter.ItemPresenter;

import android.support.annotation.NonNull;

public abstract class BaseItemPresenter<IV extends ItemView> implements ItemPresenter<IV> {

    private IV mItemView;

    @Override
    public void onCreateItemView(@NonNull IV itemView) {
        mItemView = itemView;
    }

    public IV getItemView() {
        return mItemView;
    }
}
