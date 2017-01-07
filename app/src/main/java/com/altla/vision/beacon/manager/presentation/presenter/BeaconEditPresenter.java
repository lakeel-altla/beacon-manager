package com.altla.vision.beacon.manager.presentation.presenter;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.domain.usecase.CreateAttachmentUseCase;
import com.altla.vision.beacon.manager.domain.usecase.FindAttachmentsUseCase;
import com.altla.vision.beacon.manager.domain.usecase.FindBeaconUseCase;
import com.altla.vision.beacon.manager.domain.usecase.FindProjectIdUseCase;
import com.altla.vision.beacon.manager.domain.usecase.RemoveAttachmentUseCase;
import com.altla.vision.beacon.manager.domain.usecase.UpdateDescriptionUseCase;
import com.altla.vision.beacon.manager.domain.usecase.UpdateFloorLevelUseCase;
import com.altla.vision.beacon.manager.domain.usecase.UpdatePlaceUseCase;
import com.altla.vision.beacon.manager.domain.usecase.UpdatePropertiesUseCase;
import com.altla.vision.beacon.manager.domain.usecase.UpdatePropertyUseCase;
import com.altla.vision.beacon.manager.domain.usecase.UpdateStabilityUseCase;
import com.altla.vision.beacon.manager.presentation.constants.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.presenter.mapper.AttachmentModelMapper;
import com.altla.vision.beacon.manager.presentation.presenter.mapper.BeaconModelMapper;
import com.altla.vision.beacon.manager.presentation.presenter.model.AttachmentModel;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconModel;
import com.altla.vision.beacon.manager.presentation.view.BeaconEditView;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;

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
    FindBeaconUseCase findBeaconUseCase;

    @Inject
    UpdateDescriptionUseCase updateDescriptionUseCase;

    @Inject
    UpdateFloorLevelUseCase updateFloorLevelUseCase;

    @Inject
    UpdateStabilityUseCase updateStabilityUseCase;

    @Inject
    UpdatePlaceUseCase updatePlaceUseCase;

    @Inject
    UpdatePropertyUseCase updatePropertyUseCase;

    @Inject
    UpdatePropertiesUseCase updatePropertiesUseCase;

    @Inject
    CreateAttachmentUseCase createAttachmentUseCase;

    @Inject
    RemoveAttachmentUseCase removeAttachmentUseCase;

    @Inject
    FindAttachmentsUseCase findAttachmentsUseCase;

    @Inject
    FindProjectIdUseCase findProjectIdUseCase;

    private BeaconModel beaconModel;

    private List<AttachmentModel> attachmentModels = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(BeaconEditPresenter.class);

    private BeaconModelMapper beaconModelMapper = new BeaconModelMapper();

    private AttachmentModelMapper attachmentModelMapper = new AttachmentModelMapper();

    @Inject
    BeaconEditPresenter() {
    }

    @Override
    public void onResume() {
        getView().showTitle(R.string.title_beacon_edit);
    }

    public void setBeaconModel(String beaconName) {
        BeaconModel model = new BeaconModel();
        model.beaconName = beaconName;
        beaconModel = model;
    }

    public void findBeacon() {
        Subscription subscription = findBeaconUseCase
                .execute(beaconModel.beaconName)
                .map(beaconEntity -> beaconModelMapper.map(beaconEntity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    beaconModel = model;
                    getView().updateItem(beaconModel);
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to find beacon", e);
                        getView().showSnackBar(R.string.error_process);
                    }
                });
        subscriptions.add(subscription);

        Subscription subscription1 = findAttachmentsUseCase
                .execute(beaconModel.beaconName)
                .toObservable()
                .filter(entity -> entity.attachments != null)
                .flatMap(entity -> Observable.from(entity.attachments))
                .map(entity -> attachmentModelMapper.map(entity))
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                    attachmentModels.clear();
                    attachmentModels.addAll(models);
                    getView().updateAttachments(attachmentModels, BeaconStatus.toStatus(beaconModel.status));
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to find attachments", e);
                        getView().showSnackBar(R.string.error_process);
                    }
                });
        subscriptions.add(subscription1);
    }

    public void onDescriptionInputted(String value) {
        Subscription subscription = updateDescriptionUseCase
                .execute(beaconModel.beaconName, value)
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .observeOn(AndroidSchedulers.mainThread())
                .map(entity -> beaconModelMapper.map(entity))
                .subscribe(model -> {
                    beaconModel = model;
                    getView().updateItem(beaconModel);
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to update description", e);
                        getView().showSnackBar(R.string.error_update);
                    }
                });
        subscriptions.add(subscription);
    }

    public void onFloorLevelInputted(String value) {
        Subscription subscription = updateFloorLevelUseCase
                .execute(beaconModel.beaconName, value)
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .observeOn(AndroidSchedulers.mainThread())
                .map(entity -> beaconModelMapper.map(entity))
                .subscribe(model -> {
                    beaconModel = model;
                    getView().updateItem(beaconModel);
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to update floor level", e);
                        getView().showSnackBar(R.string.error_update);
                    }
                });
        subscriptions.add(subscription);
    }

    public void onStabilityInputted(String value) {
        Subscription subscription = updateStabilityUseCase
                .execute(beaconModel.beaconName, value)
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .observeOn(AndroidSchedulers.mainThread())
                .map(entity -> beaconModelMapper.map(entity))
                .subscribe(model -> {
                    beaconModel = model;
                    getView().updateItem(beaconModel);
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to update floor level", e);
                        getView().showSnackBar(R.string.error_update);
                    }
                });
        subscriptions.add(subscription);
    }

    public void onPlaceIdClicked() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        if (beaconModel.latLng != null) {
            builder.setLatLngBounds(new LatLngBounds(beaconModel.latLng, beaconModel.latLng));
        }
        getView().showPlacePicker(builder);
    }

    public void onSelectedPlace(Place place) {
        Subscription subscription = updatePlaceUseCase
                .execute(beaconModel.beaconName, place.getId(), place.getLatLng())
                .map(entity -> beaconModelMapper.map(entity))
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    beaconModel = model;
                    getView().updateItem(beaconModel);
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to update place", e);
                        getView().showSnackBar(R.string.error_update);
                    }
                });
        subscriptions.add(subscription);
    }

    public void onPropertyInputted(String name, String value) {
        Subscription subscription = updatePropertyUseCase
                .execute(beaconModel.beaconName, name, value)
                .map(entity -> beaconModelMapper.map(entity))
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    beaconModel = model;
                    getView().updateItem(beaconModel);
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to update property", e);
                        getView().showSnackBar(R.string.error_update);
                    }
                });
        subscriptions.add(subscription);
    }

    public void onPropertyRemoved(String key) {
        Map<String, String> map = beaconModel.properties;
        map.remove(key);

        Subscription subscription = updatePropertiesUseCase
                .execute(beaconModel.beaconName, map)
                .map(entity -> beaconModelMapper.map(entity))
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    beaconModel = model;
                    getView().updateItem(beaconModel);
                }, new DefaultAuthFailCallback(this) {
                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to remove property", e);
                        getView().showSnackBar(R.string.error_remove);
                    }
                });
        subscriptions.add(subscription);
    }

    public void onAttachmentInputted(String type, String data) {
        Subscription subscription = findProjectIdUseCase
                .execute()
                .flatMap(projectId -> createAttachmentUseCase.execute(beaconModel.beaconName, projectId, type, data))
                .map(entity -> attachmentModelMapper.map(entity))
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    attachmentModels.add(model);
                    getView().updateAttachments(attachmentModels, BeaconStatus.toStatus(beaconModel.status));
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to create attachment", e);
                        getView().showSnackBar(R.string.error_add);
                    }
                });
        subscriptions.add(subscription);
    }

    public void onAttachmentRemoved(AttachmentModel model) {
        Subscription subscription = removeAttachmentUseCase
                .execute(model.attachmentName)
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    attachmentModels.remove(model);
                    getView().updateAttachments(attachmentModels, BeaconStatus.toStatus(beaconModel.status));
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to remove attachment", e);
                        getView().showSnackBar(R.string.error_remove);
                    }
                });
        subscriptions.add(subscription);
    }

    @Override
    public void refreshToken() {
        getView().refreshToken();
    }
}
