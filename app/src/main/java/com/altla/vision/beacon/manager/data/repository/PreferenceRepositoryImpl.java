package com.altla.vision.beacon.manager.data.repository;

import com.altla.vision.beacon.manager.core.StringUtils;
import com.altla.vision.beacon.manager.data.entity.PreferencesEntity;

import android.content.SharedPreferences;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;
import rx.SingleSubscriber;

public class PreferenceRepositoryImpl implements PreferenceRepository {

    private static final String ACCOUNT_NAME_KEY = "accountName";

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    EncryptedPreferences encryptedPreferences;

    @Inject
    public PreferenceRepositoryImpl() {
    }

    @Override
    public Single<String> findAccountName() {
        String accountName = sharedPreferences.getString(ACCOUNT_NAME_KEY, StringUtils.EMPTY);
        return Single.just(accountName);
    }

    @Override
    public Single<String> findToken() {
        String projectId = encryptedPreferences.getToken();
        return Single.just(projectId);
    }

    @Override
    public Single<String> findProjectId() {
        String projectId = encryptedPreferences.getProjectId();
        return Single.just(projectId);
    }

    @Override
    public Single<String> saveAccountName(String accountName) {
        return Single.create(subscriber -> {
            SharedPreferences.Editor editor = sharedPreferences.edit().putString(ACCOUNT_NAME_KEY, accountName);
            editor.commit();
            subscriber.onSuccess(accountName);
        });
    }

    @Override
    public Completable removeAccountName() {
        return Completable.create(subscriber -> {
            SharedPreferences.Editor editor = sharedPreferences.edit().remove(ACCOUNT_NAME_KEY);
            editor.apply();
            subscriber.onCompleted();
        });
    }

    @Override
    public Single<String> saveToken(String token) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                encryptedPreferences.saveToken(token);
                subscriber.onSuccess(token);
            }
        });
    }

    @Override
    public Single<String> saveProjectId(String projectId) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                encryptedPreferences.saveProjectId(projectId);
                subscriber.onSuccess(projectId);
            }
        });
    }

    @Override
    public Single<PreferencesEntity> findPreferencesData() {
        String token = encryptedPreferences.getToken();
        String projectId = encryptedPreferences.getProjectId();
        return Single.just(new PreferencesEntity(token, projectId));
    }
}
