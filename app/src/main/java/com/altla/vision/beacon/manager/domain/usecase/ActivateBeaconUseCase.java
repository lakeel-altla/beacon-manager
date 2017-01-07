package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.BeaconRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class ActivateBeaconUseCase {

    @Inject
    BeaconRepository beaconRepository;

    @Inject
    ActivateBeaconUseCase() {
    }

    public Single<Object> execute(String name) {
        return beaconRepository.activateBeacon(name).subscribeOn(Schedulers.io());
    }
}
