package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.BeaconRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class ActivateBeaconUseCase {

    @Inject
    BeaconRepository mBeaconRepository;

    @Inject
    public ActivateBeaconUseCase() {
    }

    public Single<Object> execute(String name) {
        return mBeaconRepository.activateBeacon(name).subscribeOn(Schedulers.io());
    }
}
