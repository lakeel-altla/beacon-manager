package com.altla.vision.beacon.manager.presentation;

public enum BeaconName {

    UNKNOWN("UNKNOWN"), IBEACON("IBEACON"), EDDYSTONE("EDDYSTONE"), EDDYSTONE_EID("EDDYSTONE_EID"), ALTBEACON("ALTBEACON");

    private String mValue;

    BeaconName(String value) {
        mValue = value;
    }

    public static BeaconName toBeaconName(String value) {
        for (BeaconName name : BeaconName.values()) {
            if (name.getValue().equals(value)) {
                return name;
            }
        }
        return UNKNOWN;
    }

    public String getValue() {
        return mValue;
    }
}
