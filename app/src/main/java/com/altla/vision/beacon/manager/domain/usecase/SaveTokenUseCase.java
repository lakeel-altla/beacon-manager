package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveTokenUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    public SaveTokenUseCase() {
    }

    public Single<String> execute(String token) {
        return preferenceRepository.saveToken(token).subscribeOn(Schedulers.io());
    }
}
