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
        advertisedId.type = model.mType;
        advertisedId.id = model.mBase64EncodedId;

        entity.advertisedId = advertisedId;
        entity.status = model.mStatus;
        entity.placeId = model.mPlaceId;

        if (model.mLatLng != null) {
            LatLng latLng = new LatLng();
            latLng.latitude = model.mLatLng.latitude;
            latLng.longitude = model.mLatLng.longitude;
            entity.latLng = latLng;
        }

        IndoorLevel indoorLevel = new IndoorLevel();
        indoorLevel.name = model.mFloorLevel;
        entity.indoorLevel = indoorLevel;

        entity.expectedStability = model.mStability;
        entity.description = model.mDescription;

        if (model.mProperties != null) {
            entity.properties = model.mProperties;
        }

        return entity;
    }

}
