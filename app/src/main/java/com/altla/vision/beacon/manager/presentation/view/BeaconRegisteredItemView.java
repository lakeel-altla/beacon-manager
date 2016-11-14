package com.altla.vision.beacon.manager.presentation.view;

import com.altla.vision.beacon.manager.presentation.presenter.BeaconRegisteredPresenter;
import com.altla.vision.beacon.manager.presentation.presenter.model.RegisteredBeaconModel;

public interface BeaconRegisteredItemView extends ItemView {

    void setItemPresenter(BeaconRegisteredPresenter.BeaconRegisteredItemPresenter beaconRegisteredItemPresenter);

    void updateItem(RegisteredBeaconModel model);
}
