package com.altla.vision.beacon.manager.presentation.presenter;

import android.support.annotation.IntRange;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.domain.usecase.ActivateBeaconUseCase;
import com.altla.vision.beacon.manager.domain.usecase.DeactivateBeaconUseCase;
import com.altla.vision.beacon.manager.domain.usecase.DecommissionBeaconUseCase;
import com.altla.vision.beacon.manager.domain.usecase.FindBeaconsUseCase;
import com.altla.vision.beacon.manager.presentation.constants.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.presenter.mapper.RegisteredBeaconsMapper;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconListModel;
import com.altla.vision.beacon.manager.presentation.view.BeaconListItemView;
import com.altla.vision.beacon.manager.presentation.view.BeaconListView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class BeaconRegisteredPresenter extends BasePresenter<BeaconListView> implements AuthFailure {

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

    private final List<BeaconListModel> beaconListModels = new ArrayList<>();

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

        if (totalCount <= beaconListModels.size()) {
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

                    beaconListModels.addAll(model.models);
                    sort();

                    getView().updateItems(beaconListModels);
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

                    beaconListModels.clear();
                    beaconListModels.addAll(model.models);
                    sort();

                    getView().updateItems(beaconListModels);
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to find beacons", e);
                        getView().showSnackBar(R.string.error_find);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    public void onCreateItemView(BeaconListItemView itemView) {
        BeaconListIemPresenter beaconListIemPresenter = new BeaconListIemPresenter();
        beaconListIemPresenter.onCreateItemView(itemView);
        itemView.setItemPresenter(beaconListIemPresenter);
    }

    public final class BeaconListIemPresenter extends BaseItemPresenter<BeaconListItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().updateItem(beaconListModels.get(position));
        }

        public void onClick(String name, String status) {
            getView().showBeaconEditFragment(name, BeaconStatus.toStatus(status));
        }

        public void onActivateButtonClicked(BeaconListModel model) {
            Subscription subscription = activateBeaconUseCase
                    .execute(model.name)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o -> {
                        model.status = BeaconStatus.ACTIVE.name();
                        getView().updateItems(beaconListModels);
                    }, e -> {
                        LOGGER.error("Failed to deactivate beacon", e);
                        getView().showSnackBar(R.string.error_update);
                    });
            mCompositeSubscription.add(subscription);
        }

        public void onDeactivateButtonClicked(BeaconListModel model) {
            Subscription subscription = deactivateBeaconUseCase
                    .execute(model.name)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o -> {
                        model.status = BeaconStatus.INACTIVE.name();
                        getView().updateItems(beaconListModels);
                    }, e -> {
                        LOGGER.error("Failed to deactivate beacon", e);
                        getView().showSnackBar(R.string.error_update);
                    });
            mCompositeSubscription.add(subscription);
        }

        public void onDecommissionButtonClicked(BeaconListModel model) {
            Subscription subscription = decommissionBeaconUseCase
                    .execute(model.name)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o -> {
                        model.status = BeaconStatus.DECOMMISSIONED.name();
                        getView().updateItems(beaconListModels);
                    }, e -> {
                        LOGGER.error("Failed to decommission beacon", e);
                        getView().showSnackBar(R.string.error_update);
                    });
            mCompositeSubscription.add(subscription);
        }
    }

    private void sort() {
        // Sort by beacon id.
        Collections.sort(beaconListModels, (model1, model2) -> model1.hexId.compareTo(model2.hexId));
    }
}
