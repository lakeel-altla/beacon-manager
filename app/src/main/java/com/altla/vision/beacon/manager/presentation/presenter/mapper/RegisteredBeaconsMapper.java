package com.altla.vision.beacon.manager.presentation.presenter.mapper;

import com.altla.vision.beacon.manager.core.StringUtils;
import com.altla.vision.beacon.manager.data.entity.BeaconEntity;
import com.altla.vision.beacon.manager.data.entity.BeaconsEntity;
import com.altla.vision.beacon.manager.presentation.BeaconName;
import com.altla.vision.beacon.manager.presentation.BeaconPrefix;
import com.altla.vision.beacon.manager.presentation.presenter.model.RegisteredBeaconModel;
import com.altla.vision.beacon.manager.presentation.presenter.model.RegisteredBeaconsModel;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public final class RegisteredBeaconsMapper {

    public RegisteredBeaconsModel map(@NonNull BeaconsEntity entity) {
        RegisteredBeaconsModel beaconsModel = new RegisteredBeaconsModel();

        beaconsModel.nextPageToken = entity.nextPageToken;
        beaconsModel.totalCount = entity.totalCount;

        if (entity.beacons != null) {
            beaconsModel.models = new ArrayList<>();
            for (BeaconEntity beacon : entity.beacons) {
                RegisteredBeaconModel model = new RegisteredBeaconModel();

                BeaconName name = BeaconName.toBeaconName(beacon.advertisedId.type);
                switch (name) {
                    case IBEACON:
                        model.hexId = beacon.beaconName.replace(BeaconPrefix.IBEACON.getValue(), StringUtils.EMPTY);
                        break;
                    case EDDYSTONE:
                        model.hexId = beacon.beaconName.replace(BeaconPrefix.EDDYSTONE_UID.getValue(), StringUtils.EMPTY);
                        break;
                    case EDDYSTONE_EID:
                        model.hexId = beacon.beaconName.replace(BeaconPrefix.EDDYSTONE_EID.getValue(), StringUtils.EMPTY);
                        break;
                    case ALTBEACON:
                        model.hexId = beacon.beaconName.replace(BeaconPrefix.ALTBEACON.getValue(), StringUtils.EMPTY);
                        break;
                    default:
                        break;
                }
                model.name = beacon.beaconName;
                model.type = beacon.advertisedId.type;
                model.status = beacon.status;
                model.description = beacon.description;

                beaconsModel.models.add(model);
            }
        }
        return beaconsModel;
    }
}
