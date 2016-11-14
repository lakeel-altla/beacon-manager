package com.altla.vision.beacon.manager.presentation.view;

import com.altla.vision.beacon.manager.presentation.presenter.NearbyBeaconPresenter;
import com.altla.vision.beacon.manager.presentation.presenter.model.NearbyBeaconModel;

public interface NearbyBeaconItemView extends ItemView {

    void setItemPresenter(NearbyBeaconPresenter.NearbyBeaconItemPresenter itemPresenter);

    void showItem(NearbyBeaconModel model);
}
