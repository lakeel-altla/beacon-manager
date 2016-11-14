package com.altla.vision.beacon.manager.presentation.presenter.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;

public final class BeaconModel {

    public String mBeaconName;

    public String mType;

    public String mHexId;

    public String mBase64EncodedId;

    public String mStatus;

    public String mDescription;

    public String mPlaceId;

    public String mFloorLevel;

    public String mStability;

    public LatLng mLatLng;

    public Map<String, String> mProperties;
}
