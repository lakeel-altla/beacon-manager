package com.altla.vision.beacon.manager.presentation.presenter.mapper;

import com.altla.vision.beacon.manager.presentation.BeaconName;
import com.altla.vision.beacon.manager.data.entity.BeaconEntity;
import com.altla.vision.beacon.manager.presentation.BeaconPrefix;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconModel;

import android.support.annotation.NonNull;

import java.util.HashMap;

public final class BeaconModelMapper {

    public BeaconModel map(@NonNull BeaconEntity entity) {
        BeaconModel model = new BeaconModel();

        model.mBeaconName = entity.beaconName;
        model.mType = entity.advertisedId.type;

        BeaconName name = BeaconName.toBeaconName(entity.advertisedId.type);
        switch (name) {
            case IBEACON:
                model.mHexId = entity.beaconName.replace(BeaconPrefix.IBEACON.getValue(), "");
                break;
            case EDDYSTONE:
                model.mHexId = entity.beaconName.replace(BeaconPrefix.EDDYSTONE_UID.getValue(), "");
                break;
            case EDDYSTONE_EID:
                model.mHexId = entity.beaconName.replace(BeaconPrefix.EDDYSTONE_EID.getValue(), "");
                break;
            case ALTBEACON:
                model.mHexId = entity.beaconName.replace(BeaconPrefix.ALTBEACON.getValue(), "");
                break;
            default:
                break;
        }

        model.mStatus = entity.status;
        model.mDescription = entity.description;
        model.mPlaceId = entity.placeId;

        if (entity.indoorLevel != null) {
            model.mFloorLevel = entity.indoorLevel.name;
        }

        model.mStability = entity.expectedStability;

        if (entity.properties != null) {
            if (model.mProperties == null) {
                model.mProperties = new HashMap<>();
                model.mProperties.putAll(entity.properties);
            } else {
                model.mProperties.clear();
                model.mProperties.putAll(entity.properties);
            }
        }

        return model;
    }
}
