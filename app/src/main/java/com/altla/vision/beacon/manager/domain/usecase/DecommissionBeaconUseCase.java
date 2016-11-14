package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.BeaconRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class DecommissionBeaconUseCase {

    @Inject
    BeaconRepository mBeaconRepository;

    @Inject
    public DecommissionBeaconUseCase() {
    }

    public Single<Object> execute(String name) {
        return mBeaconRepository.decommissionBeacon(name).subscribeOn(Schedulers.io());
    }

}
