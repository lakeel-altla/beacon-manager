package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.BeaconRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class DeactivateBeaconUseCase {

    @Inject
    BeaconRepository mBeaconRepository;

    @Inject
    public DeactivateBeaconUseCase() {
    }

    public Single<Object> execute(String beaconName) {
        return mBeaconRepository.deactivateBeacon(beaconName).subscribeOn(Schedulers.io());
    }

}
