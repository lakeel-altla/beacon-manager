package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.BeaconRepository;
import com.altla.vision.beacon.manager.data.repository.PreferenceRepository;
import com.altla.vision.beacon.manager.data.entity.BeaconEntity;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindBeaconUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    BeaconRepository beaconRepository;

    @Inject
    FindBeaconUseCase() {
    }

    public Single<BeaconEntity> execute(String beaconName) {
        return beaconRepository.findBeaconByName(beaconName).subscribeOn(Schedulers.io());
    }
}
