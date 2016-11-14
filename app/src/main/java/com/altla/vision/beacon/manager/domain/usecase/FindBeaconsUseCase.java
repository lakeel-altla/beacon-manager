package com.altla.vision.beacon.manager.domain.usecase;

import com.altla.vision.beacon.manager.data.repository.BeaconRepository;
import com.altla.vision.beacon.manager.data.entity.BeaconsEntity;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindBeaconsUseCase {

    @Inject
    BeaconRepository mBeaconRepository;

    @Inject
    public FindBeaconsUseCase() {
    }

    public Single<BeaconsEntity> execute(String pageToken) {
        return mBeaconRepository.findBeaconsByPageToken(pageToken).subscribeOn(Schedulers.io());
    }
}
