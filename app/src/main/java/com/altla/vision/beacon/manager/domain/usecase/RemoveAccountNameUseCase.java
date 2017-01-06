package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;

public final class RemoveAccountNameUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    public RemoveAccountNameUseCase() {
    }

    public Completable execute() {
        return preferenceRepository.removeAccountName();
    }
}
