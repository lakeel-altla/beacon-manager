package com.altla.vision.beacon.manager.presentation.view;

import android.content.Intent;
import android.support.annotation.StringRes;

public interface SignInView {

    void showTitle(@StringRes int resId);

    void showGooglePlayErrorDialog(int statusCode);

    void showOAuthActivity(Intent intent);

    void showBeaconScanFragment();

    void showSnackBar(@StringRes int resId);
}
