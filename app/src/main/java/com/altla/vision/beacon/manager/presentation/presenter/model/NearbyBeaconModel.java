package com.altla.vision.beacon.manager.presentation.presenter.model;

import com.altla.vision.beacon.manager.presentation.BeaconType;

public final class NearbyBeaconModel {

    public String hexId;

    public String base64EncodedId;

    public BeaconType type;

    public int rssi;
}
