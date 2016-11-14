package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveAccountNameUseCase {

    @Inject
    PreferenceRepository mPreferenceRepository;

    @Inject
    public SaveAccountNameUseCase() {
    }

    public Single<String> execute(String accountName) {
        return mPreferenceRepository.saveAccountName(accountName).subscribeOn(Schedulers.io());
    }
}
