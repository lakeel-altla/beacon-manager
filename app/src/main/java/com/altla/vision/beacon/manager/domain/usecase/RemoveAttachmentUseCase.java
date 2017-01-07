package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.domain.repository.BeaconRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class RemoveAttachmentUseCase {

    @Inject
    BeaconRepository beaconRepository;

    @Inject
    RemoveAttachmentUseCase() {
    }

    public Single<Object> execute(String attachmentName) {
        return beaconRepository.removeAttachment(attachmentName).subscribeOn(Schedulers.io());
    }
}
