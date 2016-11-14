package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.BeaconRepository;
import com.altla.vision.beacon.manager.data.entity.BeaconEntity;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class UpdateFloorLevelUseCase {

    @Inject
    BeaconRepository mBeaconRepository;

    @Inject
    public UpdateFloorLevelUseCase() {
    }

    public Single<BeaconEntity> execute(String beaconName, String floorLevel) {
        return mBeaconRepository.findBeaconByName(beaconName)
                .map(beaconEntity -> {
                    if (beaconEntity.indoorLevel == null) {
                        beaconEntity.indoorLevel = new BeaconEntity.IndoorLevel();
                        beaconEntity.indoorLevel.name = floorLevel;
                    } else {
                        beaconEntity.indoorLevel.name = floorLevel;
                    }
                    return beaconEntity;
                })
                .flatMap(this::updateBeacon)
                .subscribeOn(Schedulers.io());
    }

    Single<BeaconEntity> updateBeacon(BeaconEntity beaconEntity) {
        return mBeaconRepository.updateBeacon(beaconEntity);
    }
}
