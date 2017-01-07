package com.altla.vision.beacon.manager.data.repository;

import com.altla.vision.beacon.manager.data.entity.BeaconAttachmentEntity;
import com.altla.vision.beacon.manager.data.entity.BeaconAttachmentsEntity;
import com.altla.vision.beacon.manager.data.entity.BeaconEntity;
import com.altla.vision.beacon.manager.data.entity.BeaconsEntity;
import com.altla.vision.beacon.manager.data.entity.NamespacesEntity;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconModel;

import rx.Single;

public interface BeaconRepository {

    Single<BeaconEntity> findBeaconByName(String beaconName);

    Single<BeaconsEntity> findBeaconsByPageToken(String pageToken);

    Single<NamespacesEntity> findNamespaces();

    Single<BeaconAttachmentsEntity> findAttachments(String beaconName);

    Single<BeaconEntity> registerBeacon(BeaconModel model);

    Single<BeaconEntity> updateBeacon(BeaconEntity beaconEntity);

    Single<BeaconAttachmentEntity> createAttachment(String beaconName, String projectId, String type, String value);

    Single<Object> removeAttachment(String attachmentName);

    Single<Object> activateBeacon(String beaconName);

    Single<Object> deactivateBeacon(String beaconName);

    Single<Object> decommissionBeacon(String beaconName);
}
