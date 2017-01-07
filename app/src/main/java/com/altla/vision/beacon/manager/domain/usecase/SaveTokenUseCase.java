package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.domain.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveTokenUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    SaveTokenUseCase() {
    }

    public Single<String> execute(String token) {
        return preferenceRepository.saveToken(token).subscribeOn(Schedulers.io());
    }
}
