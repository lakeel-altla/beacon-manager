package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.domain.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Completable;

public final class RemoveAccountNameUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    RemoveAccountNameUseCase() {
    }

    public Completable execute() {
        return preferenceRepository.removeAccountName();
    }
}
