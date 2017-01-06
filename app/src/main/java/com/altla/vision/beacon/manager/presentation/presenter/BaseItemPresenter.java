package com.altla.vision.beacon.manager.presentation.presenter;

import android.support.annotation.NonNull;

import com.altla.vision.beacon.manager.presentation.view.ItemView;

abstract class BaseItemPresenter<IV extends ItemView> implements ItemPresenter<IV> {

    private IV itemView;

    @Override
    public void onCreateItemView(@NonNull IV itemView) {
        this.itemView = itemView;
    }

    IV getItemView() {
        return itemView;
    }
}
