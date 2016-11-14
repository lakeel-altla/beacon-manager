package com.altla.vision.beacon.manager.data.entity.mapper;

import com.altla.vision.beacon.manager.core.StringUtils;
import com.altla.vision.beacon.manager.data.entity.BeaconAttachmentEntity;

public final class BeaconAttachmentMapper {

    public BeaconAttachmentEntity map(String nameSpacedType, String value) {
        BeaconAttachmentEntity entity = new BeaconAttachmentEntity();
        entity.namespacedType = nameSpacedType;
        entity.data = StringUtils.toBase64Encoded(value.getBytes());
        return entity;
    }
}
