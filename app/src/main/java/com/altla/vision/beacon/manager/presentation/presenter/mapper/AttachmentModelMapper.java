package com.altla.vision.beacon.manager.presentation.presenter.mapper;

import com.altla.vision.beacon.manager.core.ByteUtils;
import com.altla.vision.beacon.manager.data.entity.BeaconAttachmentEntity;
import com.altla.vision.beacon.manager.presentation.presenter.model.AttachmentModel;

import android.support.annotation.NonNull;

public final class AttachmentModelMapper {

    public AttachmentModel map(@NonNull BeaconAttachmentEntity entity) {
        AttachmentModel model = new AttachmentModel();

        String nameSpacedType = entity.namespacedType;

        int namespacePosition = nameSpacedType.indexOf("/");
        String namespace = nameSpacedType.substring(0, namespacePosition);
        String type = nameSpacedType.substring(namespacePosition + 1, nameSpacedType.length());

        model.attachmentName = entity.attachmentName;
        model.namespace = namespace;
        model.type = type;
        model.data = new String(ByteUtils.toBase64Decoded(entity.data));
        return model;
    }
}
