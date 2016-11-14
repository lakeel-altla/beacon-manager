package com.altla.vision.beacon.manager.data;

public enum HttpStatus {

    UNKNOWN(0), NOT_AUTHORIZED(401), CONFLICT(409);

    private int mValue;

    HttpStatus(int value) {
        mValue = value;
    }

    public static HttpStatus toHttpStatus(int value) {
        for (HttpStatus status : HttpStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }

        return UNKNOWN;
    }

    public int getValue() {
        return mValue;
    }
}
