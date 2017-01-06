package com.altla.vision.beacon.manager.presentation.presenter;


import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.altla.vision.beacon.manager.presentation.view.ItemView;

interface ItemPresenter<IV extends ItemView> {

    void onCreateItemView(@NonNull IV itemView);

    void onBind(@IntRange(from = 0) int position);
}
