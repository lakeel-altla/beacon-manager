package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.domain.repository.BeaconRepository;
import com.altla.vision.beacon.manager.data.entity.BeaconAttachmentsEntity;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindAttachmentsUseCase {

    @Inject
    BeaconRepository beaconRepository;

    @Inject
    FindAttachmentsUseCase() {
    }

    public Single<BeaconAttachmentsEntity> execute(String beaconName) {
        return beaconRepository.findAttachments(beaconName).subscribeOn(Schedulers.io());
    }
}
