package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindTokenUseCase {

    @Inject
    PreferenceRepository mPreferenceRepository;

    @Inject
    public FindTokenUseCase() {
    }

    public Single<String> execute() {
        return mPreferenceRepository.findToken().subscribeOn(Schedulers.io());
    }
}
