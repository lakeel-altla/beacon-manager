package com.altla.vision.beacon.manager.presentation.view;

import com.google.android.gms.location.places.ui.PlacePicker;

import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconModel;

import android.support.annotation.StringRes;

public interface BeaconRegisterView extends AuthView {

    void showTitle(@StringRes int resId);

    void showSnackBar(@StringRes int resId);

    void showProgressDialog();

    void hideProgressDialog();

    void updateItem(BeaconModel model);

    void showPlacePicker(PlacePicker.IntentBuilder builder);

    void showBeaconRegisteredFragment();
}
