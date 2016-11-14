package com.altla.vision.beacon.manager.presentation.presenter;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.core.StringUtils;
import com.altla.vision.beacon.manager.domain.usecase.FindAccountNameUseCase;
import com.altla.vision.beacon.manager.domain.usecase.FindTokenUseCase;
import com.altla.vision.beacon.manager.domain.usecase.RemoveAccountNameUseCase;
import com.altla.vision.beacon.manager.domain.usecase.SaveTokenUseCase;
import com.altla.vision.beacon.manager.presentation.view.ActivityView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.accounts.Account;
import android.content.Context;

import java.io.IOException;

import javax.inject.Inject;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class ActivityPresenter extends BasePresenter<ActivityView> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityPresenter.class);

    @Inject
    FindTokenUseCase mFindTokenUseCase;

    @Inject
    RemoveAccountNameUseCase mRemoveAccountNameUseCase;

    @Inject
    FindAccountNameUseCase mFindAccountNameUseCase;

    @Inject
    SaveTokenUseCase mSaveTokenUseCase;

    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userlocation.beacon.registry";

    @Inject
    public ActivityPresenter() {
    }

    public void checkAuthentication(Context context) {
        // アカウント名(メールアドレス)が preference に保持されていれば、
        // サインイン済みだと判定する。
        mFindAccountNameUseCase
                .execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accountName -> {
                    if (StringUtils.isEmpty(accountName)) {
                        getView().showSignInFragment();
                    } else {
                        // token を更新。
                        saveToken(context);
                        getView().showBeaconScanFragment();
                    }
                }, e -> LOGGER.error("Failed to findByBeaconName account mName", e));
    }

    public void saveToken(Context context) {
        Subscription subscription = mFindAccountNameUseCase
                .execute()
                .flatMap(accountName -> getToken(context, accountName))
                .flatMap(token -> mSaveTokenUseCase.execute(token))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                }, e -> {
                    LOGGER.error("Failed to save new token", e);
                    getView().showSnackBar(R.string.error_process);
                });
        mCompositeSubscription.add(subscription);
    }

    public void refreshToken(Context context) {

        // Proximity Beacon API は各 Fragment から呼び出すことになり、
        // いずれも token の有効期限切れ対応が必要になるため、Activity 側でまとめて対応。

        Subscription subscription = mFindTokenUseCase
                .execute()
                .flatMap(oldToken -> clearToken(context, oldToken))
                .flatMap(s -> mFindAccountNameUseCase.execute())
                .flatMap(accountName -> getToken(context, accountName))
                .flatMap(token -> mSaveTokenUseCase.execute(token))
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
        Subscription subscription = mFindTokenUseCase
                .execute()
                .flatMap(token -> clearToken(context, token))
                .flatMap(token -> mRemoveAccountNameUseCase.execute().toSingle(() -> token))
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
