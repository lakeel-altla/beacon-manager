package com.altla.vision.beacon.manager.data.exception;

public class RefreshTokenException extends RuntimeException {

    public RefreshTokenException(Exception e) {
        super(e);
    }
}
