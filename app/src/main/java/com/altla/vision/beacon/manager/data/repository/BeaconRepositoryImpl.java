package com.altla.vision.beacon.manager.data.repository;

import com.altla.vision.beacon.manager.data.entity.BeaconAttachmentEntity;
import com.altla.vision.beacon.manager.data.entity.BeaconAttachmentsEntity;
import com.altla.vision.beacon.manager.data.entity.BeaconEntity;
import com.altla.vision.beacon.manager.data.entity.BeaconsEntity;
import com.altla.vision.beacon.manager.data.entity.NamespacesEntity;
import com.altla.vision.beacon.manager.data.entity.mapper.BeaconAttachmentMapper;
import com.altla.vision.beacon.manager.data.entity.mapper.BeaconEntityMapper;
import com.altla.vision.beacon.manager.data.repository.retrofit.api.ProximityApi;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconModel;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Single;

public class BeaconRepositoryImpl implements BeaconRepository {

    private BeaconEntityMapper beaconEntityMapper = new BeaconEntityMapper();

    private BeaconAttachmentMapper beaconAttachmentMapper = new BeaconAttachmentMapper();

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    Retrofit retrofit;

    @Inject
    BeaconRepositoryImpl() {
    }

    @Override
    public Single<BeaconsEntity> findBeaconsByPageToken(String pageToken) {
        return retrofit.create(ProximityApi.class).find(pageToken);
    }

    @Override
    public Single<NamespacesEntity> findNamespaces() {
        return retrofit.create(ProximityApi.class).findNamespaces();
    }

    @Override
    public Single<BeaconAttachmentsEntity> findAttachments(String beaconName) {
        return retrofit.create(ProximityApi.class).findAttachments(beaconName);
    }

    @Override
    public Single<BeaconEntity> registerBeacon(BeaconModel model) {
        BeaconEntity entity = beaconEntityMapper.map(model);
        return retrofit.create(ProximityApi.class).registerBeacon(entity);
    }

    @Override
    public Single<BeaconEntity> updateBeacon(BeaconEntity entity) {
        return retrofit.create(ProximityApi.class).updateBeacon(entity.beaconName, entity);
    }

    @Override
    public Single<BeaconAttachmentEntity> createAttachment(String beaconName, String projectId, String type, String value) {
        BeaconAttachmentEntity entity = beaconAttachmentMapper.map(projectId, type, value);
        return retrofit.create(ProximityApi.class).createAttachment(beaconName, entity);
    }

    @Override
    public Single<Object> removeAttachment(String attachmentName) {
        return retrofit.create(ProximityApi.class).removeAttachment(attachmentName);
    }

    @Override
    public Single<Object> activateBeacon(String beaconName) {
        return retrofit.create(ProximityApi.class).activateBeacon(beaconName);
    }

    @Override
    public Single<Object> deactivateBeacon(String beaconName) {
        return retrofit.create(ProximityApi.class).deactivateBeacon(beaconName);
    }

    @Override
    public Single<Object> decommissionBeacon(String beaconName) {
        return retrofit.create(ProximityApi.class).deactivateBeacon(beaconName);
    }

    @Override
    public Single<BeaconEntity> findBeaconByName(String beaconName) {
        return retrofit.create(ProximityApi.class).findBeacon(beaconName);
    }
}
