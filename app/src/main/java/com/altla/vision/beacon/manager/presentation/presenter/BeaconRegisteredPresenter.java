package com.altla.vision.beacon.manager.presentation.presenter;

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

import android.support.annotation.IntRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class BeaconRegisteredPresenter extends BasePresenter<RegisteredBeaconView> implements AuthFailure {

    @Inject
    FindBeaconsUseCase mFindBeaconsUseCase;

    @Inject
    DeactivateBeaconUseCase mDeactivateBeaconUseCase;

    @Inject
    ActivateBeaconUseCase mActivateBeaconUseCase;

    @Inject
    DecommissionBeaconUseCase mDecommissionBeaconUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(BeaconRegisteredPresenter.class);

    private String mNextPageToken;

    private int mTotalCount;

    private boolean mIsNextBeaconsFetching;

    private final List<RegisteredBeaconModel> mRegisteredBeaconModels = new ArrayList<>();

    private RegisteredBeaconsMapper mMapper = new RegisteredBeaconsMapper();

    @Inject
    public BeaconRegisteredPresenter() {
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
        if (mIsNextBeaconsFetching) {
            return;
        }

        mIsNextBeaconsFetching = true;

        if (mTotalCount <= mRegisteredBeaconModels.size()) {
            mIsNextBeaconsFetching = false;
            return;
        }

        Subscription subscription = mFindBeaconsUseCase
                .execute(mNextPageToken)
                .map(beaconsEntity -> mMapper.map(beaconsEntity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    mIsNextBeaconsFetching = false;
                    mTotalCount = Integer.parseInt(model.mTotalCount);
                    mNextPageToken = model.mNextPageToken;

                    // 追加取得を行うため、リストに追加する。clear してはならない。
                    mRegisteredBeaconModels.addAll(model.mModels);
                    sort();

                    getView().updateItems(mRegisteredBeaconModels);
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to find beacons", e);

                        mIsNextBeaconsFetching = false;
                        getView().showSnackBar(R.string.error_find);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    private void findLatestBeacons() {
        Subscription subscription = mFindBeaconsUseCase
                .execute(null)
                .map(beaconsEntity -> mMapper.map(beaconsEntity))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> getView().showRefreshProgress())
                .doOnUnsubscribe(() -> getView().hideRefreshProgress())
                .subscribe(model -> {
                    mTotalCount = Integer.parseInt(model.mTotalCount);
                    mNextPageToken = model.mNextPageToken;

                    mRegisteredBeaconModels.clear();
                    mRegisteredBeaconModels.addAll(model.mModels);
                    sort();

                    getView().updateItems(mRegisteredBeaconModels);
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
            getItemView().updateItem(mRegisteredBeaconModels.get(position));
        }

        public void onClick(String name, String status) {
            getView().showBeaconEditFragment(name, BeaconStatus.toStatus(status));
        }

        public void onActivateButtonClicked(RegisteredBeaconModel model) {
            Subscription subscription = mActivateBeaconUseCase
                    .execute(model.mName)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o -> {
                        model.mStatus = BeaconStatus.ACTIVE.name();
                        getView().updateItems(mRegisteredBeaconModels);
                    }, e -> {
                        LOGGER.error("Failed to deactivate beacon", e);
                        getView().showSnackBar(R.string.error_update);
                    });
            mCompositeSubscription.add(subscription);
        }

        public void onDeactivateButtonClicked(RegisteredBeaconModel model) {
            Subscription subscription = mDeactivateBeaconUseCase
                    .execute(model.mName)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o -> {
                        model.mStatus = BeaconStatus.INACTIVE.name();
                        getView().updateItems(mRegisteredBeaconModels);
                    }, e -> {
                        LOGGER.error("Failed to deactivate beacon", e);
                        getView().showSnackBar(R.string.error_update);
                    });
            mCompositeSubscription.add(subscription);
        }

        public void onDecommissionButtonClicked(RegisteredBeaconModel model) {
            Subscription subscription = mDecommissionBeaconUseCase
                    .execute(model.mName)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o -> {
                        model.mStatus = BeaconStatus.DECOMMISSIONED.name();
                        getView().updateItems(mRegisteredBeaconModels);
                    }, e -> {
                        LOGGER.error("Failed to decommission beacon", e);
                        getView().showSnackBar(R.string.error_update);
                    });
            mCompositeSubscription.add(subscription);
        }
    }

    private void sort() {
        // ID でソート。
        Collections.sort(mRegisteredBeaconModels, (model1, model2) -> model1.mHexId.compareTo(model2.mHexId));
    }
}
