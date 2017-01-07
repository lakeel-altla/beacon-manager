package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.entity.BeaconEntity;
import com.altla.vision.beacon.manager.domain.repository.BeaconRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class UpdateStabilityUseCase {

    @Inject
    BeaconRepository beaconRepository;

    @Inject
    UpdateStabilityUseCase() {
    }

    public Single<BeaconEntity> execute(String beaconName, String value) {
        return beaconRepository.findBeaconByName(beaconName)
                .map(beaconEntity -> {
                    beaconEntity.expectedStability = value;
                    return beaconEntity;
                })
                .flatMap(this::updateBeacon)
                .subscribeOn(Schedulers.io());
    }

    Single<BeaconEntity> updateBeacon(BeaconEntity beaconEntity) {
        return beaconRepository.updateBeacon(beaconEntity);
    }
}
