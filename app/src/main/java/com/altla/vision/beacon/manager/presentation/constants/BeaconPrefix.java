package com.altla.vision.beacon.manager.presentation.constants;

public enum BeaconPrefix {

    IBEACON("beacons/1!"), EDDYSTONE_UID("beacons/3!"), EDDYSTONE_EID("beacons/4!"), ALTBEACON("beacons/5!");

    private String value;

    BeaconPrefix(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
