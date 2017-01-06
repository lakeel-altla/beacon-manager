package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.BeaconRepository;
import com.altla.vision.beacon.manager.data.entity.BeaconAttachmentEntity;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class CreateAttachmentUseCase {

    @Inject
    BeaconRepository beaconRepository;

    @Inject
    public CreateAttachmentUseCase() {
    }

    public Single<BeaconAttachmentEntity> execute(String beaconName, String type, String data) {
        return beaconRepository.createAttachment(beaconName, type, data).subscribeOn(Schedulers.io());
    }
}
