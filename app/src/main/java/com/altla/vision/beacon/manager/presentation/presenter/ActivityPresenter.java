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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.inject.Inject;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class ActivityPresenter extends BasePresenter<ActivityView> {

    @Inject
    FindTokenUseCase findTokenUseCase;

    @Inject
    RemoveAccountNameUseCase removeAccountNameUseCase;

    @Inject
    FindAccountNameUseCase findAccountNameUseCase;

    @Inject
    SaveTokenUseCase saveTokenUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityPresenter.class);

    private Context context;

    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userlocation.beacon.registry";

    @Inject
    ActivityPresenter(Context context) {
        this.context = context;
    }

    public void onSignIn() {
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
                        saveToken();
                        getView().showNearbyBeaconFragment();
                    }
                }, e -> LOGGER.error("Failed to findByBeaconName account name", e));
    }

    public void saveToken() {
        Subscription subscription = findAccountNameUseCase
                .execute()
                .flatMap(this::getToken)
                .flatMap(token -> saveTokenUseCase.execute(token))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                }, e -> {
                    LOGGER.error("Failed to save new token", e);
                    getView().showSnackBar(R.string.error_process);
                });
        subscriptions.add(subscription);
    }

    public void onSignOut() {
        Subscription subscription = findTokenUseCase
                .execute()
                .flatMap(this::clearToken)
                .flatMap(token -> removeAccountNameUseCase.execute().toSingle(() -> token))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> getView().showSignInFragment(),
                        e -> {
                            LOGGER.error("Failed to sign out", e);
                            getView().showSnackBar(R.string.error_sign_out);
                        });
        subscriptions.add(subscription);
    }

    private Single<String> clearToken(String token) {
        return Single.create(subscriber -> {
            try {
                GoogleAuthUtil.clearToken(context, token);
                subscriber.onSuccess(token);
            } catch (GoogleAuthException | IOException e) {
                subscriber.onError(e);
            }
        });
    }

    private Single<String> getToken(String accountName) {
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
