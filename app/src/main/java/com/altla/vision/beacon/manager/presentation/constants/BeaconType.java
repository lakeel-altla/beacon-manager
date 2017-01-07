package com.altla.vision.beacon.manager.presentation.constants;

public enum BeaconType {

    IBEACON("IBEACON"), EDDYSTONE("EDDYSTONE");

    private String value;

    BeaconType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
