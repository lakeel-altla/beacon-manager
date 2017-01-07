package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.BeaconRepository;
import com.altla.vision.beacon.manager.data.entity.BeaconEntity;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class UpdatePropertyUseCase {

    @Inject
    BeaconRepository beaconRepository;

    @Inject
    UpdatePropertyUseCase() {
    }

    public Single<BeaconEntity> execute(String beaconName, String name, String value) {
        return beaconRepository
                .findBeaconByName(beaconName)
                .map(entity -> {
                    if (entity.properties == null) {
                        entity.properties = new HashMap<>();
                        entity.properties.put(name, value);
                    } else {
                        entity.properties.put(name, value);
                    }
                    return entity;
                })
                .flatMap(this::updateBeacon)
                .subscribeOn(Schedulers.io());
    }

    Single<BeaconEntity> updateBeacon(BeaconEntity beaconEntity) {
        return beaconRepository.updateBeacon(beaconEntity);
    }
}
