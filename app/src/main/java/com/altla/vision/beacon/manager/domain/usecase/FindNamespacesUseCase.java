package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.BeaconRepository;
import com.altla.vision.beacon.manager.data.repository.PreferenceRepository;
import com.altla.vision.beacon.manager.data.entity.NamespacesEntity;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindNamespacesUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    BeaconRepository beaconRepository;

    @Inject
    FindNamespacesUseCase() {
    }

    public Single<NamespacesEntity> execute() {
        return beaconRepository.findNamespaces().subscribeOn(Schedulers.io());
    }
}
