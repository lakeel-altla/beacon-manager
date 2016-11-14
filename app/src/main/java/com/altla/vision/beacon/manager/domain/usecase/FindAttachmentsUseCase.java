package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.BeaconRepository;
import com.altla.vision.beacon.manager.data.entity.BeaconAttachmentsEntity;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindAttachmentsUseCase {

    @Inject
    BeaconRepository mBeaconRepository;

    @Inject
    public FindAttachmentsUseCase() {
    }

    public Single<BeaconAttachmentsEntity> execute(String beaconName) {
        return mBeaconRepository.findAttachments(beaconName).subscribeOn(Schedulers.io());
    }
}
