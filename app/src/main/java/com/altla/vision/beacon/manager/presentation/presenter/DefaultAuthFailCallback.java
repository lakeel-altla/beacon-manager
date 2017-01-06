package com.altla.vision.beacon.manager.presentation.presenter;

import com.altla.vision.beacon.manager.data.exception.NotAuthorizedException;

import rx.functions.Action1;

public abstract class DefaultAuthFailCallback implements Action1<Throwable> {

    private AuthFailure authFailure;

    public DefaultAuthFailCallback(AuthFailure failure) {
        authFailure = failure;
    }

    @Override
    public void call(Throwable e) {
        if (e instanceof NotAuthorizedException) {
            authFailure.refreshToken();
        } else {
            onError(e);
        }
    }

    abstract void onError(Throwable e);
}
