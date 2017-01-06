package com.altla.vision.beacon.manager.data.entity.mapper;

import com.altla.vision.beacon.manager.data.entity.BeaconEntity;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconModel;

import static com.altla.vision.beacon.manager.data.entity.BeaconEntity.AdvertisedId;
import static com.altla.vision.beacon.manager.data.entity.BeaconEntity.IndoorLevel;
import static com.altla.vision.beacon.manager.data.entity.BeaconEntity.LatLng;

public final class BeaconEntityMapper {

    public BeaconEntity map(BeaconModel model) {
        BeaconEntity entity = new BeaconEntity();

        // NOTE:
        // Not set beacon name when register a beacon.

        AdvertisedId advertisedId = new AdvertisedId();
        advertisedId.type = model.type;
        advertisedId.id = model.base64EncodedId;

        entity.advertisedId = advertisedId;
        entity.status = model.status;
        entity.placeId = model.placeId;

        if (model.latLng != null) {
            LatLng latLng = new LatLng();
            latLng.latitude = model.latLng.latitude;
            latLng.longitude = model.latLng.longitude;
            entity.latLng = latLng;
        }

        IndoorLevel indoorLevel = new IndoorLevel();
        indoorLevel.name = model.floorLevel;
        entity.indoorLevel = indoorLevel;

        entity.expectedStability = model.stability;
        entity.description = model.description;

        if (model.properties != null) {
            entity.properties = model.properties;
        }

        return entity;
    }

}
