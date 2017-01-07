package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.domain.repository.BeaconRepository;
import com.altla.vision.beacon.manager.data.entity.BeaconEntity;

import java.util.Map;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class UpdatePropertiesUseCase {

    @Inject
    BeaconRepository beaconRepository;

    @Inject
    UpdatePropertiesUseCase() {
    }

    public Single<BeaconEntity> execute(String beaconName, Map<String, String> properties) {
        return beaconRepository
                .findBeaconByName(beaconName)
                .map(entity -> {
                    entity.properties.clear();
                    entity.properties.putAll(properties);
                    return entity;
                })
                .flatMap(this::updateBeacon)
                .subscribeOn(Schedulers.io());
    }

    Single<BeaconEntity> updateBeacon(BeaconEntity beaconEntity) {
        return beaconRepository.updateBeacon(beaconEntity);
    }
}
