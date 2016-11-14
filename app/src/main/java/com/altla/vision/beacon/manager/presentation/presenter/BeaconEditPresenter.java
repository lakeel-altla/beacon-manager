package com.altla.vision.beacon.manager.presentation.presenter;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.domain.usecase.CreateAttachmentUseCase;
import com.altla.vision.beacon.manager.domain.usecase.FindAttachmentsUseCase;
import com.altla.vision.beacon.manager.domain.usecase.FindBeaconUseCase;
import com.altla.vision.beacon.manager.domain.usecase.RemoveAttachmentUseCase;
import com.altla.vision.beacon.manager.domain.usecase.UpdateDescriptionUseCase;
import com.altla.vision.beacon.manager.domain.usecase.UpdateFloorLevelUseCase;
import com.altla.vision.beacon.manager.domain.usecase.UpdatePlaceUseCase;
import com.altla.vision.beacon.manager.domain.usecase.UpdatePropertiesUseCase;
import com.altla.vision.beacon.manager.domain.usecase.UpdatePropertyUseCase;
import com.altla.vision.beacon.manager.domain.usecase.UpdateStabilityUseCase;
import com.altla.vision.beacon.manager.presentation.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.presenter.mapper.AttachmentModelMapper;
import com.altla.vision.beacon.manager.presentation.presenter.mapper.BeaconModelMapper;
import com.altla.vision.beacon.manager.presentation.presenter.model.AttachmentModel;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconModel;
import com.altla.vision.beacon.manager.presentation.view.BeaconEditView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class BeaconEditPresenter extends BasePresenter<BeaconEditView> implements AuthFailure {

    @Inject
    FindBeaconUseCase mFindBeaconUseCase;

    @Inject
    UpdateDescriptionUseCase mUpdateDescriptionUseCase;

    @Inject
    UpdateFloorLevelUseCase mUpdateFloorLevelUseCase;

    @Inject
    UpdateStabilityUseCase mUpdateStabilityUseCase;

    @Inject
    UpdatePlaceUseCase mUpdatePlaceUseCase;

    @Inject
    UpdatePropertyUseCase mUpdatePropertyUseCase;

    @Inject
    UpdatePropertiesUseCase mUpdatePropertiesUseCase;

    @Inject
    CreateAttachmentUseCase mCreateAttachmentUseCase;

    @Inject
    RemoveAttachmentUseCase mRemoveAttachmentUseCase;

    @Inject
    FindAttachmentsUseCase mFindAttachmentsUseCase;

    private BeaconModel mBeaconModel;

    private List<AttachmentModel> mAttachmentModels = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(BeaconEditPresenter.class);

    private BeaconModelMapper mBeaconModelMapper = new BeaconModelMapper();

    private AttachmentModelMapper mAttachmentModelMapper = new AttachmentModelMapper();

    @Inject
    public BeaconEditPresenter() {
    }

    @Override
    public void onResume() {
        getView().showTitle(R.string.title_beacon_edit);
    }

    public void setBeaconModel(String beaconName) {
        BeaconModel model = new BeaconModel();
        model.mBeaconName = beaconName;
        mBeaconModel = model;
    }

    public void findBeacon() {
        Subscription subscription = mFindBeaconUseCase
                .execute(mBeaconModel.mBeaconName)
                .map(beaconEntity -> mBeaconModelMapper.map(beaconEntity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    mBeaconModel = model;
                    getView().updateItem(mBeaconModel);
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to find beacon", e);
                        getView().showSnackBar(R.string.error_process);
                    }
                });
        mCompositeSubscription.add(subscription);

        Subscription subscription1 = mFindAttachmentsUseCase
                .execute(mBeaconModel.mBeaconName)
                .toObservable()
                .filter(entity -> entity.attachments != null)
                .flatMap(entity -> Observable.from(entity.attachments))
                .map(entity -> mAttachmentModelMapper.map(entity))
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                    mAttachmentModels.clear();
                    mAttachmentModels.addAll(models);
                    getView().updateAttachments(mAttachmentModels, BeaconStatus.toStatus(mBeaconModel.mStatus));
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to find attachments", e);
                        getView().showSnackBar(R.string.error_process);
                    }
                });
        mCompositeSubscription.add(subscription1);
    }

    public void onDescriptionInputted(String value) {
        Subscription subscription = mUpdateDescriptionUseCase
                .execute(mBeaconModel.mBeaconName, value)
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .observeOn(AndroidSchedulers.mainThread())
                .map(entity -> mBeaconModelMapper.map(entity))
                .subscribe(model -> {
                    mBeaconModel = model;
                    getView().updateItem(mBeaconModel);
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to update mDescription", e);
                        getView().showSnackBar(R.string.error_update);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    public void onFloorLevelInputted(String value) {
        Subscription subscription = mUpdateFloorLevelUseCase
                .execute(mBeaconModel.mBeaconName, value)
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .observeOn(AndroidSchedulers.mainThread())
                .map(entity -> mBeaconModelMapper.map(entity))
                .subscribe(model -> {
                    mBeaconModel = model;
                    getView().updateItem(mBeaconModel);
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to update floor level", e);
                        getView().showSnackBar(R.string.error_update);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    public void onStabilityInputted(String value) {
        Subscription subscription = mUpdateStabilityUseCase
                .execute(mBeaconModel.mBeaconName, value)
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .observeOn(AndroidSchedulers.mainThread())
                .map(entity -> mBeaconModelMapper.map(entity))
                .subscribe(model -> {
                    mBeaconModel = model;
                    getView().updateItem(mBeaconModel);
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to update floor level", e);
                        getView().showSnackBar(R.string.error_update);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    public void onPlaceIdClicked() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        if (mBeaconModel.mLatLng != null) {
            builder.setLatLngBounds(new LatLngBounds(mBeaconModel.mLatLng, mBeaconModel.mLatLng));
        }
        getView().showPlacePicker(builder);
    }

    public void onSelectedPlace(Place place) {
        Subscription subscription = mUpdatePlaceUseCase
                .execute(mBeaconModel.mBeaconName, place.getId(), place.getLatLng())
                .map(entity -> mBeaconModelMapper.map(entity))
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    mBeaconModel = model;
                    getView().updateItem(mBeaconModel);
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to update place", e);
                        getView().showSnackBar(R.string.error_update);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    public void onPropertyInputted(String name, String value) {
        Subscription subscription = mUpdatePropertyUseCase
                .execute(mBeaconModel.mBeaconName, name, value)
                .map(entity -> mBeaconModelMapper.map(entity))
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    mBeaconModel = model;
                    getView().updateItem(mBeaconModel);
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to update property", e);
                        getView().showSnackBar(R.string.error_update);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    public void onPropertyRemoved(String key) {
        Map<String, String> map = mBeaconModel.mProperties;
        map.remove(key);

        Subscription subscription = mUpdatePropertiesUseCase
                .execute(mBeaconModel.mBeaconName, map)
                .map(entity -> mBeaconModelMapper.map(entity))
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    mBeaconModel = model;
                    getView().updateItem(mBeaconModel);
                }, new DefaultAuthFailCallback(this) {
                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to remove property", e);
                        getView().showSnackBar(R.string.error_remove);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    public void onAttachmentInputted(String type, String data) {
        Subscription subscription = mCreateAttachmentUseCase
                .execute(mBeaconModel.mBeaconName, type, data)
                .map(entity -> mAttachmentModelMapper.map(entity))
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    mAttachmentModels.add(model);
                    getView().updateAttachments(mAttachmentModels, BeaconStatus.toStatus(mBeaconModel.mStatus));
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to create attachment", e);
                        getView().showSnackBar(R.string.error_add);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    public void onAttachmentRemoved(AttachmentModel model) {
        Subscription subscription = mRemoveAttachmentUseCase
                .execute(model.mAttachmentName)
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    mAttachmentModels.remove(model);
                    getView().updateAttachments(mAttachmentModels, BeaconStatus.toStatus(mBeaconModel.mStatus));
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to remove attachment", e);
                        getView().showSnackBar(R.string.error_remove);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void refreshToken() {
        getView().refreshToken();
    }
}
