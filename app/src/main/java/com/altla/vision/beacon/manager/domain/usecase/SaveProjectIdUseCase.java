package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.PreferenceRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveProjectIdUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    public SaveProjectIdUseCase() {
    }

    public Single<String> execute(String projectId) {
        return preferenceRepository.saveProjectId(projectId).subscribeOn(Schedulers.io());
    }
}
