package com.altla.vision.beacon.manager.presentation.presenter.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;

public final class BeaconModel {

    public String beaconName;

    public String type;

    public String hexId;

    public String base64EncodedId;

    public String status;

    public String description;

    public String placeId;

    public String floorLevel;

    public String stability;

    public LatLng latLng;

    public Map<String, String> properties;
}
