package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.domain.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindAccountNameUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    FindAccountNameUseCase() {
    }

    public Single<String> execute() {
        return preferenceRepository.findAccountName().subscribeOn(Schedulers.io());
    }
}
