package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.domain.repository.BeaconRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class DeactivateBeaconUseCase {

    @Inject
    BeaconRepository beaconRepository;

    @Inject
    DeactivateBeaconUseCase() {
    }

    public Single<Object> execute(String beaconName) {
        return beaconRepository.deactivateBeacon(beaconName).subscribeOn(Schedulers.io());
    }

}
