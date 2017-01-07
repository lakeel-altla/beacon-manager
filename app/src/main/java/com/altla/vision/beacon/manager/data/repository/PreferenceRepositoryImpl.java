package com.altla.vision.beacon.manager.data.repository;

import com.altla.vision.beacon.manager.domain.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;
import rx.SingleSubscriber;

public class PreferenceRepositoryImpl implements PreferenceRepository {

    @Inject
    EncryptedPreferences encryptedPreferences;

    @Inject
    public PreferenceRepositoryImpl() {
    }

    @Override
    public Single<String> findAccountName() {
        String accountName = encryptedPreferences.getAccountName();
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
            encryptedPreferences.saveAccountName(accountName);
            subscriber.onSuccess(accountName);
        });
    }

    @Override
    public Completable removeAccountName() {
        return Completable.create(subscriber -> {
            encryptedPreferences.removeAccountName();
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
}
