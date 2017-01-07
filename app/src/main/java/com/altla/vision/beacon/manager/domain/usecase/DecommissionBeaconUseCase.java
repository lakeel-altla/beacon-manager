package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.BeaconRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class DecommissionBeaconUseCase {

    @Inject
    BeaconRepository beaconRepository;

    @Inject
    DecommissionBeaconUseCase() {
    }

    public Single<Object> execute(String name) {
        return beaconRepository.decommissionBeacon(name).subscribeOn(Schedulers.io());
    }

}
