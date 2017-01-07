package com.altla.vision.beacon.manager.data.repository;

import rx.Completable;
import rx.Single;

public interface PreferenceRepository {

    Single<String> findAccountName();

    Single<String> findToken();

    Single<String> findProjectId();

    Single<String> saveAccountName(String accountName);

    Completable removeAccountName();

    Single<String> saveToken(String token);

    Single<String> saveProjectId(String nameSpace);
}
