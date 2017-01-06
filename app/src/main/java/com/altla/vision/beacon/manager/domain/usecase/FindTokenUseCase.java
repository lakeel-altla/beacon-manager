package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindTokenUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    public FindTokenUseCase() {
    }

    public Single<String> execute() {
        return preferenceRepository.findToken().subscribeOn(Schedulers.io());
    }
}
