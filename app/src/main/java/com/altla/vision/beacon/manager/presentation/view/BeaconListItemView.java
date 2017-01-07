package com.altla.vision.beacon.manager.presentation.view;

import com.altla.vision.beacon.manager.presentation.presenter.BeaconListPresenter;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconListModel;

public interface BeaconListItemView extends ItemView {

    void setItemPresenter(BeaconListPresenter.BeaconListIemPresenter beaconListIemPresenter);

    void updateItem(BeaconListModel model);
}
