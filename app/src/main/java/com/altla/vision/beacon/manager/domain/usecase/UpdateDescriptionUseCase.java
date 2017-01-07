package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.domain.repository.BeaconRepository;
import com.altla.vision.beacon.manager.data.entity.BeaconEntity;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class UpdateDescriptionUseCase {

    @Inject
    BeaconRepository beaconRepository;

    @Inject
    UpdateDescriptionUseCase() {
    }

    public Single<BeaconEntity> execute(String beaconName, String description) {
        return beaconRepository.findBeaconByName(beaconName)
                .map(beaconEntity -> {
                    beaconEntity.description = description;
                    return beaconEntity;
                })
                .flatMap(this::updateBeacon)
                .subscribeOn(Schedulers.io());
    }

    Single<BeaconEntity> updateBeacon(BeaconEntity beaconEntity) {
        return beaconRepository.updateBeacon(beaconEntity);
    }
}
