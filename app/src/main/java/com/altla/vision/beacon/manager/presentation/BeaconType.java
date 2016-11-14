package com.altla.vision.beacon.manager.presentation;

public enum BeaconType {

    UNKNOWN("UNKNOWN"), IBEACON("IBEACON"), EDDYSTONE("EDDYSTONE");

    private String mValue;

    BeaconType(String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
