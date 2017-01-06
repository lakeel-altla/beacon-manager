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

        model.beaconName = entity.beaconName;
        model.type = entity.advertisedId.type;

        BeaconName name = BeaconName.toBeaconName(entity.advertisedId.type);
        switch (name) {
            case IBEACON:
                model.hexId = entity.beaconName.replace(BeaconPrefix.IBEACON.getValue(), "");
                break;
            case EDDYSTONE:
                model.hexId = entity.beaconName.replace(BeaconPrefix.EDDYSTONE_UID.getValue(), "");
                break;
            case EDDYSTONE_EID:
                model.hexId = entity.beaconName.replace(BeaconPrefix.EDDYSTONE_EID.getValue(), "");
                break;
            case ALTBEACON:
                model.hexId = entity.beaconName.replace(BeaconPrefix.ALTBEACON.getValue(), "");
                break;
            default:
                break;
        }

        model.status = entity.status;
        model.description = entity.description;
        model.placeId = entity.placeId;

        if (entity.indoorLevel != null) {
            model.floorLevel = entity.indoorLevel.name;
        }

        model.stability = entity.expectedStability;

        if (entity.properties != null) {
            if (model.properties == null) {
                model.properties = new HashMap<>();
                model.properties.putAll(entity.properties);
            } else {
                model.properties.clear();
                model.properties.putAll(entity.properties);
            }
        }

        return model;
    }
}
