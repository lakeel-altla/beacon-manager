package com.altla.vision.beacon.manager.presentation.view;

import com.altla.vision.beacon.manager.presentation.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.presenter.model.RegisteredBeaconModel;

import android.support.annotation.StringRes;

import java.util.List;

public interface RegisteredBeaconView extends AuthView {

    void showTitle(@StringRes int resId);

    void showBeaconEditFragment(String name, BeaconStatus beaconStatus);

    void updateItems(List<RegisteredBeaconModel> mRegisteredBeaconModels);

    void showSnackBar(@StringRes int resId);

    void showRefreshProgress();

    void hideRefreshProgress();
}
