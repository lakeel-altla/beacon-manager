package com.altla.vision.beacon.manager.presentation.presenter;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.domain.usecase.SaveAccountNameUseCase;
import com.altla.vision.beacon.manager.domain.usecase.SaveTokenUseCase;
import com.altla.vision.beacon.manager.presentation.view.SignInView;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class SignInPresenter extends BasePresenter<SignInView> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignInPresenter.class);

    private static final String AUTH_SCOPE = "oauth2:https://www.googleapis.com/auth/userlocation.beacon.registry";

    @Inject
    SaveAccountNameUseCase saveAccountNameUseCase;

    @Inject
    SaveTokenUseCase saveTokenUseCase;

    @Inject
    public SignInPresenter() {
    }

    @Override
    public void onResume() {
        getView().showTitle(R.string.title_sign_in);
    }

    public void onPikedUpAccount(Activity activity, String accountName) {
        Subscription subscription = authenticate(activity.getApplicationContext(), accountName)
                .flatMap(this::saveToken)
                .flatMap(s -> saveAccountName(accountName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> getView().showNearbyBeaconFragment(),
                        e -> {
                            // Check authorized result.
                            if (e instanceof UserRecoverableAuthException) {
                                if (e instanceof GooglePlayServicesAvailabilityException) {
                                    int statusCode = ((GooglePlayServicesAvailabilityException) e).getConnectionStatusCode();
                                    getView().showGooglePlayErrorDialog(statusCode);
                                } else {
                                    Intent intent = ((UserRecoverableAuthException) e).getIntent();
                                    getView().showOAuthActivity(intent);
                                }
                            } else {
                                LOGGER.error("Failed to saveAccountName to google", e);
                                getView().showSnackBar(R.string.error_sign_in);
                            }
                        });
        subscriptions.add(subscription);
    }

    Single<String> authenticate(Context context, String accountName) {
        return Single.create(new Single.OnSubscribe<String>() {

            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                try {
                    Account account = new Account(accountName, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
                    String token = GoogleAuthUtil.getToken(context, account, AUTH_SCOPE);
                    subscriber.onSuccess(token);
                } catch (IOException | GoogleAuthException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    Single<String> saveAccountName(String accountName) {
        return saveAccountNameUseCase.execute(accountName).subscribeOn(Schedulers.io());
    }

    Single<String> saveToken(String token) {
        return saveTokenUseCase.execute(token).subscribeOn(Schedulers.io());
    }
}