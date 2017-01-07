package com.altla.vision.beacon.manager.presentation.view;

import android.support.annotation.StringRes;

public interface ActivityView {

    void showSignInFragment();

    void showNearbyBeaconFragment();

    void showSnackBar(@StringRes int resId);
}
