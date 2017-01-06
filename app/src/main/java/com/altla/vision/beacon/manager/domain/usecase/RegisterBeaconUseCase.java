package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconModel;
import com.altla.vision.beacon.manager.data.repository.BeaconRepository;
import com.altla.vision.beacon.manager.data.entity.BeaconEntity;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class RegisterBeaconUseCase {

    @Inject
    BeaconRepository beaconRepository;

    @Inject
    public RegisterBeaconUseCase() {
    }

    public Single<BeaconEntity> execute(BeaconModel model) {
        return beaconRepository.registerBeacon(model).subscribeOn(Schedulers.io());
    }
}
