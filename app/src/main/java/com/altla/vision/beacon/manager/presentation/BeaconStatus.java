package com.altla.vision.beacon.manager.presentation;

public enum BeaconStatus {

    UNKNOWN("UNKNOWN"), ACTIVE("ACTIVE"), DECOMMISSIONED("DECOMMISSIONED"), INACTIVE("INACTIVE");

    private String value;

    BeaconStatus(String value) {
        this.value = value;
    }

    public static BeaconStatus toStatus(String value) {
        for (BeaconStatus beaconStatus : BeaconStatus.values()) {
            if (beaconStatus.value.equals(value)) {
                return beaconStatus;
            }
        }
        return UNKNOWN;
    }

    public String getValue() {
        return value;
    }
}
