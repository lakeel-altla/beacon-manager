package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveTokenUseCase {

    @Inject
    PreferenceRepository mPreferenceRepository;

    @Inject
    public SaveTokenUseCase() {
    }

    public Single<String> execute(String token) {
        return mPreferenceRepository.saveToken(token).subscribeOn(Schedulers.io());
    }
}
