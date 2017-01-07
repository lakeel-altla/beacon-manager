package com.altla.vision.beacon.manager.data;

public enum Header {
    AUTHORIZATION("Authorization");

    private String value;

    Header(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
