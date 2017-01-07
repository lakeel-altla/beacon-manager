package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.domain.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveAccountNameUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    SaveAccountNameUseCase() {
    }

    public Single<String> execute(String accountName) {
        return preferenceRepository.saveAccountName(accountName).subscribeOn(Schedulers.io());
    }
}
