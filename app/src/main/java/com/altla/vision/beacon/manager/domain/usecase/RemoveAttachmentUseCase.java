package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.BeaconRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class RemoveAttachmentUseCase {

    @Inject
    BeaconRepository mBeaconRepository;

    @Inject
    public RemoveAttachmentUseCase() {
    }

    public Single<Object> execute(String attachmentName) {
        return mBeaconRepository.removeAttachment(attachmentName).subscribeOn(Schedulers.io());
    }
}
