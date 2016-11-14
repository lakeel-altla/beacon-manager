package com.altla.vision.beacon.manager.data.repository;

import com.altla.vision.beacon.manager.data.entity.BeaconAttachmentEntity;
import com.altla.vision.beacon.manager.data.entity.BeaconAttachmentsEntity;
import com.altla.vision.beacon.manager.data.entity.BeaconEntity;
import com.altla.vision.beacon.manager.data.entity.BeaconsEntity;
import com.altla.vision.beacon.manager.data.entity.NamespacesEntity;
import com.altla.vision.beacon.manager.data.entity.PreferencesEntity;
import com.altla.vision.beacon.manager.data.repository.retrofit.api.ProximityApi;
import com.altla.vision.beacon.manager.data.entity.mapper.BeaconAttachmentMapper;
import com.altla.vision.beacon.manager.data.entity.mapper.BeaconEntityMapper;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconModel;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Retrofit;
import rx.Single;

public class BeaconRepositoryImpl implements BeaconRepository {

    private BeaconEntityMapper mBeaconEntityMapper = new BeaconEntityMapper();

    private BeaconAttachmentMapper mBeaconAttachmentMapper = new BeaconAttachmentMapper();

    @Inject
    PreferenceRepository mPreferenceRepository;

    @Inject
    public Retrofit mRetrofit;

    @Inject
    @Named("bearer")
    public String bearer;

    @Inject
    public BeaconRepositoryImpl() {
    }

    @Override
    public Single<BeaconsEntity> findBeaconsByPageToken(String pageToken) {
        return getPreferencesData()
                .flatMap(data -> findBeaconsByPageToken(data.mToken, data.mProjectId, pageToken));
    }

    @Override
    public Single<NamespacesEntity> findNamespaces() {
        return getPreferencesData()
                .flatMap(data -> findNamespaces(data.mToken));
    }

    @Override
    public Single<BeaconAttachmentsEntity> findAttachments(String beaconName) {
        return getPreferencesData()
                .flatMap(data -> mRetrofit.create(ProximityApi.class).findAttachments(bearer + data.mToken, beaconName));
    }

    @Override
    public Single<BeaconEntity> registerBeacon(BeaconModel model) {
        return getPreferencesData()
                .flatMap(data -> registerBeacon(data.mToken, data.mProjectId, model));
    }

    @Override
    public Single<BeaconEntity> updateBeacon(BeaconEntity beaconEntity) {
        return getPreferencesData()
                .flatMap(data -> updateBeacon(data.mToken, data.mProjectId, beaconEntity));
    }

    @Override
    public Single<BeaconAttachmentEntity> createAttachment(String beaconName, String type, String value) {
        return getPreferencesData().
                flatMap(data -> createBeaconAttachment(data.mToken, data.mProjectId, beaconName, data.mProjectId + "/" + type, value));
    }

    @Override
    public Single<Object> removeAttachment(String attachmentName) {
        return getPreferencesData()
                .flatMap(data -> removeAttachment(data.mToken, data.mProjectId, attachmentName));
    }

    @Override
    public Single<Object> activateBeacon(String beaconName) {
        return getPreferencesData()
                .flatMap(data -> activateBeacon(data.mToken, data.mProjectId, beaconName));
    }

    @Override
    public Single<Object> deactivateBeacon(String beaconName) {
        return getPreferencesData()
                .flatMap(data -> deactivateBeacon(data.mToken, data.mProjectId, beaconName));
    }

    @Override
    public Single<Object> decommissionBeacon(String beaconName) {
        return getPreferencesData()
                .flatMap(data -> decommissionBeacon(data.mToken, data.mProjectId, beaconName));
    }

    @Override
    public Single<BeaconEntity> findBeaconByName(String beaconName) {
        return getPreferencesData()
                .flatMap(data -> findBeaconByName(data.mToken, data.mProjectId, beaconName));
    }

    Single<PreferencesEntity> getPreferencesData() {
        return mPreferenceRepository.findPreferencesData();
    }

    Single<BeaconsEntity> findBeaconsByPageToken(String token, String projectId, String pageToken) {
        return mRetrofit.create(ProximityApi.class).find(bearer + token, projectId, pageToken);
    }

    Single<NamespacesEntity> findNamespaces(String token) {
        return mRetrofit.create(ProximityApi.class).findNamespaces(bearer + token);
    }

    Single<BeaconEntity> findBeaconByName(String token, String projectId, String beaconName) {
        return mRetrofit.create(ProximityApi.class).findBeacon(bearer + token, beaconName, projectId);
    }

    Single<BeaconEntity> registerBeacon(String token, String projectId, BeaconModel model) {
        BeaconEntity entity = mBeaconEntityMapper.map(model);
        return mRetrofit.create(ProximityApi.class).registerBeacon(bearer + token, projectId, entity);
    }

    Single<BeaconEntity> updateBeacon(String token, String projectId, BeaconEntity entity) {
        return mRetrofit.create(ProximityApi.class).updateBeacon(bearer + token, entity.beaconName, projectId, entity);
    }

    Single<BeaconAttachmentEntity> createBeaconAttachment(String token, String projectId, String beaconName, String nameSpacedType, String value) {
        BeaconAttachmentEntity entity = mBeaconAttachmentMapper.map(nameSpacedType, value);
        return mRetrofit.create(ProximityApi.class).createAttachment(bearer + token, beaconName, projectId, entity);
    }

    Single<Object> removeAttachment(String token, String projectId, String attachmentName) {
        return mRetrofit.create(ProximityApi.class).removeAttachment(bearer + token, attachmentName, projectId);
    }

    Single<Object> activateBeacon(String token, String projectId, String beaconName) {
        return mRetrofit.create(ProximityApi.class).activateBeacon(bearer + token, beaconName, projectId);
    }

    Single<Object> deactivateBeacon(String token, String projectId, String beaconName) {
        return mRetrofit.create(ProximityApi.class).deactivateBeacon(bearer + token, beaconName, projectId);
    }

    Single<Object> decommissionBeacon(String token, String projectId, String beaconName) {
        return mRetrofit.create(ProximityApi.class).decommissionBeacon(bearer + token, beaconName, projectId);
    }
}
