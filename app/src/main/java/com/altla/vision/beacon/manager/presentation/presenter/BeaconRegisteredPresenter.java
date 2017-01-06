package com.altla.vision.beacon.manager.presentation.presenter;

import android.support.annotation.IntRange;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.domain.usecase.ActivateBeaconUseCase;
import com.altla.vision.beacon.manager.domain.usecase.DeactivateBeaconUseCase;
import com.altla.vision.beacon.manager.domain.usecase.DecommissionBeaconUseCase;
import com.altla.vision.beacon.manager.domain.usecase.FindBeaconsUseCase;
import com.altla.vision.beacon.manager.presentation.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.presenter.mapper.RegisteredBeaconsMapper;
import com.altla.vision.beacon.manager.presentation.presenter.model.RegisteredBeaconModel;
import com.altla.vision.beacon.manager.presentation.view.BeaconRegisteredItemView;
import com.altla.vision.beacon.manager.presentation.view.RegisteredBeaconView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class BeaconRegisteredPresenter extends BasePresenter<RegisteredBeaconView> implements AuthFailure {

    @Inject
    FindBeaconsUseCase findBeaconsUseCase;

    @Inject
    DeactivateBeaconUseCase deactivateBeaconUseCase;

    @Inject
    ActivateBeaconUseCase activateBeaconUseCase;

    @Inject
    DecommissionBeaconUseCase decommissionBeaconUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(BeaconRegisteredPresenter.class);

    private String nextPageToken;

    private int totalCount;

    private boolean isNextBeaconsFetching;

    private final List<RegisteredBeaconModel> registeredBeaconModels = new ArrayList<>();

    private RegisteredBeaconsMapper mapper = new RegisteredBeaconsMapper();

    @Inject
    BeaconRegisteredPresenter() {
    }

    @Override
    public void onResume() {
        getView().showTitle(R.string.title_registered);
        findLatestBeacons();
    }

    @Override
    public void refreshToken() {
        getView().refreshToken();
    }

    public void onRefreshFromTop() {
        findLatestBeacons();
    }

    public void onRefreshFromBottom() {
        if (isNextBeaconsFetching) {
            return;
        }

        isNextBeaconsFetching = true;

        if (totalCount <= registeredBeaconModels.size()) {
            isNextBeaconsFetching = false;
            return;
        }

        Subscription subscription = findBeaconsUseCase
                .execute(nextPageToken)
                .map(beaconsEntity -> mapper.map(beaconsEntity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    isNextBeaconsFetching = false;
                    totalCount = Integer.parseInt(model.totalCount);
                    nextPageToken = model.nextPageToken;

                    registeredBeaconModels.addAll(model.models);
                    sort();

                    getView().updateItems(registeredBeaconModels);
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to find beacons", e);

                        isNextBeaconsFetching = false;
                        getView().showSnackBar(R.string.error_find);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    private void findLatestBeacons() {
        Subscription subscription = findBeaconsUseCase
                .execute(null)
                .map(beaconsEntity -> mapper.map(beaconsEntity))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> getView().showRefreshProgress())
                .doOnUnsubscribe(() -> getView().hideRefreshProgress())
                .subscribe(model -> {
                    totalCount = Integer.parseInt(model.totalCount);
                    nextPageToken = model.nextPageToken;

                    registeredBeaconModels.clear();
                    registeredBeaconModels.addAll(model.models);
                    sort();

                    getView().updateItems(registeredBeaconModels);
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to find beacons", e);
                        getView().showSnackBar(R.string.error_find);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    public void onCreateItemView(BeaconRegisteredItemView itemView) {
        BeaconRegisteredItemPresenter beaconRegisteredItemPresenter = new BeaconRegisteredItemPresenter();
        beaconRegisteredItemPresenter.onCreateItemView(itemView);
        itemView.setItemPresenter(beaconRegisteredItemPresenter);
    }

    public final class BeaconRegisteredItemPresenter extends BaseItemPresenter<BeaconRegisteredItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().updateItem(registeredBeaconModels.get(position));
        }

        public void onClick(String name, String status) {
            getView().showBeaconEditFragment(name, BeaconStatus.toStatus(status));
        }

        public void onActivateButtonClicked(RegisteredBeaconModel model) {
            Subscription subscription = activateBeaconUseCase
                    .execute(model.name)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o -> {
                        model.status = BeaconStatus.ACTIVE.name();
                        getView().updateItems(registeredBeaconModels);
                    }, e -> {
                        LOGGER.error("Failed to deactivate beacon", e);
                        getView().showSnackBar(R.string.error_update);
                    });
            mCompositeSubscription.add(subscription);
        }

        public void onDeactivateButtonClicked(RegisteredBeaconModel model) {
            Subscription subscription = deactivateBeaconUseCase
                    .execute(model.name)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o -> {
                        model.status = BeaconStatus.INACTIVE.name();
                        getView().updateItems(registeredBeaconModels);
                    }, e -> {
                        LOGGER.error("Failed to deactivate beacon", e);
                        getView().showSnackBar(R.string.error_update);
                    });
            mCompositeSubscription.add(subscription);
        }

        public void onDecommissionButtonClicked(RegisteredBeaconModel model) {
            Subscription subscription = decommissionBeaconUseCase
                    .execute(model.name)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o -> {
                        model.status = BeaconStatus.DECOMMISSIONED.name();
                        getView().updateItems(registeredBeaconModels);
                    }, e -> {
                        LOGGER.error("Failed to decommission beacon", e);
                        getView().showSnackBar(R.string.error_update);
                    });
            mCompositeSubscription.add(subscription);
        }
    }

    private void sort() {
        // Sort by beacon id.
        Collections.sort(registeredBeaconModels, (model1, model2) -> model1.hexId.compareTo(model2.hexId));
    }
}
