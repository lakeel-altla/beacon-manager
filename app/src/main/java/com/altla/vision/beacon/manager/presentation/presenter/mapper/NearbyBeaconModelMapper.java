package com.altla.vision.beacon.manager.presentation.presenter.mapper;

import com.altla.vision.beacon.manager.core.ByteUtils;
import com.altla.vision.beacon.manager.core.StringUtils;
import com.altla.vision.beacon.manager.presentation.BeaconType;
import com.altla.vision.beacon.manager.presentation.presenter.model.NearbyBeaconModel;

import org.altbeacon.beacon.Beacon;

public final class NearbyBeaconModelMapper {

    public NearbyBeaconModel map(Beacon beacon) {
        NearbyBeaconModel nearbyBeaconModel = new NearbyBeaconModel();
        nearbyBeaconModel.mRssi = beacon.getRssi();

        int serviceUuid = beacon.getServiceUuid();
        int beaconTypeCode = beacon.getBeaconTypeCode();
        if (serviceUuid == 0xfeaa && beaconTypeCode == 0x00) {
            // This is a Eddystone-UID frame
            byte[] namespaceIdAsByte = beacon.getId1().toByteArray();
            byte[] instanceIdAsByte = beacon.getId2().toByteArray();
            byte[] id = ByteUtils.concat(namespaceIdAsByte, instanceIdAsByte);

            nearbyBeaconModel.mHexId = StringUtils.toHex(id);
            nearbyBeaconModel.mBase64EncodedId = StringUtils.toBase64Encoded(id);
            nearbyBeaconModel.mType = BeaconType.EDDYSTONE;
        } else if (serviceUuid == 0xfeaa && beaconTypeCode == 0x30) {
            // This is a Eddystone-EID frame
            byte[] id = beacon.getId1().toByteArray();

            nearbyBeaconModel.mHexId = StringUtils.toHex(id);
            nearbyBeaconModel.mBase64EncodedId = StringUtils.toBase64Encoded(id);
            nearbyBeaconModel.mType = BeaconType.EDDYSTONE;
        } else if (serviceUuid == -1 && beaconTypeCode == 533) {
            // This is a iBeacon frame
            byte[] uuid = beacon.getId1().toByteArray();
            byte[] major = beacon.getId2().toByteArray();
            byte[] minor = beacon.getId3().toByteArray();
            byte[] id = ByteUtils.concat(uuid, major, minor);

            nearbyBeaconModel.mHexId = StringUtils.toHex(id);
            nearbyBeaconModel.mBase64EncodedId = StringUtils.toBase64Encoded(id);
            nearbyBeaconModel.mType = BeaconType.IBEACON;
        }

        return nearbyBeaconModel;
    }
}
