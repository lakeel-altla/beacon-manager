package com.altla.vision.beacon.manager.presentation.view;

import android.support.annotation.IntRange;

public interface ItemView {

    void onBind(@IntRange(from = 0) int position);
}
