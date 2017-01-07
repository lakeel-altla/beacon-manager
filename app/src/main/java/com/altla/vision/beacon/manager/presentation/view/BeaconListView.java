package com.altla.vision.beacon.manager.presentation.view;

import android.support.annotation.StringRes;

import com.altla.vision.beacon.manager.presentation.constants.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconListModel;

import java.util.List;

public interface BeaconListView {

    void showTitle(@StringRes int resId);

    void showBeaconEditFragment(String name, BeaconStatus beaconStatus);

    void updateItems(List<BeaconListModel> mBeaconListModels);

    void showSnackBar(@StringRes int resId);

    void showRefreshProgress();

    void hideRefreshProgress();
}
