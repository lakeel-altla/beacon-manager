package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.entity.BeaconAttachmentEntity;
import com.altla.vision.beacon.manager.data.repository.BeaconRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class CreateAttachmentUseCase {

    @Inject
    BeaconRepository beaconRepository;

    @Inject
    CreateAttachmentUseCase() {
    }

    public Single<BeaconAttachmentEntity> execute(String beaconName, String projectId, String type, String data) {
        return beaconRepository.createAttachment(beaconName, projectId, type, data).subscribeOn(Schedulers.io());
    }
}
