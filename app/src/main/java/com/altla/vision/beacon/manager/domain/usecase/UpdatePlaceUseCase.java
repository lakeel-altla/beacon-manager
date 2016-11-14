package com.altla.vision.beacon.manager.domain.usecase;


import com.google.android.gms.maps.model.LatLng;

import com.altla.vision.beacon.manager.data.repository.BeaconRepository;
import com.altla.vision.beacon.manager.data.entity.BeaconEntity;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class UpdatePlaceUseCase {

    @Inject
    BeaconRepository mBeaconRepository;

    @Inject
    public UpdatePlaceUseCase() {
    }

    public Single<BeaconEntity> execute(String beaconName, String placeId, LatLng latLng) {
        return mBeaconRepository.findBeaconByName(beaconName)
                .map(beaconEntity -> {
                    beaconEntity.placeId = placeId;
                    beaconEntity.latLng = new BeaconEntity.LatLng();
                    beaconEntity.latLng.latitude = latLng.latitude;
                    beaconEntity.latLng.longitude = latLng.longitude;
                    return beaconEntity;
                })
                .flatMap(this::updateBeacon)
                .subscribeOn(Schedulers.io());
    }

    Single<BeaconEntity> updateBeacon(BeaconEntity beaconEntity) {
        return mBeaconRepository.updateBeacon(beaconEntity);
    }
}
