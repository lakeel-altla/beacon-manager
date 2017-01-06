package com.altla.vision.beacon.manager.presentation.presenter;

import android.accounts.Account;
import android.content.Context;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.core.StringUtils;
import com.altla.vision.beacon.manager.domain.usecase.FindAccountNameUseCase;
import com.altla.vision.beacon.manager.domain.usecase.FindTokenUseCase;
import com.altla.vision.beacon.manager.domain.usecase.RemoveAccountNameUseCase;
import com.altla.vision.beacon.manager.domain.usecase.SaveTokenUseCase;
import com.altla.vision.beacon.manager.presentation.view.ActivityView;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.inject.Inject;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class ActivityPresenter extends BasePresenter<ActivityView> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityPresenter.class);

    @Inject
    FindTokenUseCase findTokenUseCase;

    @Inject
    RemoveAccountNameUseCase removeAccountNameUseCase;

    @Inject
    FindAccountNameUseCase findAccountNameUseCase;

    @Inject
    SaveTokenUseCase saveTokenUseCase;

    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userlocation.beacon.registry";

    @Inject
    ActivityPresenter() {
    }

    public void checkAuthentication(Context context) {
        //　If the account name (mail address) is saved in the preference,
        //  already signed in.
        findAccountNameUseCase
                .execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accountName -> {
                    if (StringUtils.isEmpty(accountName)) {
                        getView().showSignInFragment();
                    } else {
                        // Update the token.
                        saveToken(context);
                        getView().showBeaconScanFragment();
                    }
                }, e -> LOGGER.error("Failed to findByBeaconName account name", e));
    }

    public void saveToken(Context context) {
        Subscription subscription = findAccountNameUseCase
                .execute()
                .flatMap(accountName -> getToken(context, accountName))
                .flatMap(token -> saveTokenUseCase.execute(token))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                }, e -> {
                    LOGGER.error("Failed to save new token", e);
                    getView().showSnackBar(R.string.error_process);
                });
        mCompositeSubscription.add(subscription);
    }

    public void refreshToken(Context context) {
        Subscription subscription = findTokenUseCase
                .execute()
                .flatMap(oldToken -> clearToken(context, oldToken))
                .flatMap(s -> findAccountNameUseCase.execute())
                .flatMap(accountName -> getToken(context, accountName))
                .flatMap(token -> saveTokenUseCase.execute(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(token -> getView().showSnackBar(R.string.message_try_again),
                        e -> {
                            LOGGER.error("Failed to refresh token", e);
                            if (e instanceof UserRecoverableAuthException) {
                                getView().showUserRecoverableAuthDialog(((UserRecoverableAuthException) e).getIntent());
                            } else {
                                getView().showSnackBar(R.string.error_process);
                            }
                        });
        mCompositeSubscription.add(subscription);
    }

    public void onSignOut(Context context) {
        Subscription subscription = findTokenUseCase
                .execute()
                .flatMap(token -> clearToken(context, token))
                .flatMap(token -> removeAccountNameUseCase.execute().toSingle(() -> token))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> getView().showSignInFragment(),
                        e -> {
                            LOGGER.error("Failed to sign out", e);
                            getView().showSnackBar(R.string.error_sign_out);
                        });
        mCompositeSubscription.add(subscription);
    }

    Single<String> clearToken(Context context, String token) {
        return Single.create(subscriber -> {
            try {
                GoogleAuthUtil.clearToken(context, token);
                subscriber.onSuccess(token);
            } catch (GoogleAuthException | IOException e) {
                subscriber.onError(e);
            }
        });
    }

    Single<String> getToken(Context context, String accountName) {
        return Single.create(subscriber -> {
            try {
                Account account = new Account(accountName, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
                String token = GoogleAuthUtil.getToken(context, account, SCOPE);
                subscriber.onSuccess(token);
            } catch (IOException | GoogleAuthException e) {
                subscriber.onError(e);
            }
        });
    }
}
