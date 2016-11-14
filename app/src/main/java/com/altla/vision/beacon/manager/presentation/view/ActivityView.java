package com.altla.vision.beacon.manager.presentation.view;

import android.content.Intent;
import android.support.annotation.StringRes;

public interface ActivityView {

    void showSignInFragment();

    void showBeaconScanFragment();

    void showUserRecoverableAuthDialog(Intent intent);

    void showSnackBar(@StringRes int resId);
}
