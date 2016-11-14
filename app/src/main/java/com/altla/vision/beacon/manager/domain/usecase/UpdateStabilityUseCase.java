package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.BeaconRepository;
import com.altla.vision.beacon.manager.data.entity.BeaconEntity;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class UpdateStabilityUseCase {

    @Inject
    BeaconRepository mBeaconRepository;

    @Inject
    public UpdateStabilityUseCase() {
    }

    public Single<BeaconEntity> execute(String beaconName, String value) {
        return mBeaconRepository.findBeaconByName(beaconName)
                .map(beaconEntity -> {
                    beaconEntity.expectedStability = value;
                    return beaconEntity;
                })
                .flatMap(this::updateBeacon)
                .subscribeOn(Schedulers.io());
    }

    Single<BeaconEntity> updateBeacon(BeaconEntity beaconEntity) {
        return mBeaconRepository.updateBeacon(beaconEntity);
    }
}
