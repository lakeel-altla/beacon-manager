package com.altla.vision.beacon.manager.data;

public enum Header {

    CONTENT_TYPE("Content-Type"), AUTHORIZATION("Authorization");

    String mValue;

    Header(String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
