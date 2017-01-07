package com.altla.vision.beacon.manager.presentation.view;

import android.support.annotation.IntRange;
import android.support.annotation.StringRes;

public interface NearbyBeaconView {

    void showTitle(@StringRes int resId);

    void showBleEnabledActivity();

    void updateItems();

    void removeAllItems(@IntRange(from = 0) int size);

    void showSnackBar(@StringRes int resId);

    void showBeaconRegisterFragment(String type, String hexId, String base64EncodedId);
}
