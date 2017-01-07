package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindProjectIdUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    FindProjectIdUseCase() {
    }

    public Single<String> execute() {
        return preferenceRepository.findProjectId().subscribeOn(Schedulers.io());
    }
}
