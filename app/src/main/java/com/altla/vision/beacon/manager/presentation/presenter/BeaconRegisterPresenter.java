package com.altla.vision.beacon.manager.presentation.presenter;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.data.exception.ConflictException;
import com.altla.vision.beacon.manager.domain.usecase.CreateAttachmentUseCase;
import com.altla.vision.beacon.manager.domain.usecase.RegisterBeaconUseCase;
import com.altla.vision.beacon.manager.domain.usecase.RemoveAttachmentUseCase;
import com.altla.vision.beacon.manager.presentation.constants.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconModel;
import com.altla.vision.beacon.manager.presentation.view.BeaconRegisterView;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class BeaconRegisterPresenter extends BasePresenter<BeaconRegisterView> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeaconRegisterPresenter.class);

    private BeaconModel beaconModel;

    @Inject
    RegisterBeaconUseCase registerBeaconUseCase;

    @Inject
    CreateAttachmentUseCase createAttachmentUseCase;

    @Inject
    RemoveAttachmentUseCase removeAttachmentUseCase;

    @Inject
    BeaconRegisterPresenter() {
    }

    @Override
    public void onResume() {
        getView().showTitle(R.string.title_beacon_register);
        getView().updateItem(beaconModel);
    }

    public void setBeaconModel(String type, String hexId, String base64EncodedId) {
        BeaconModel model = new BeaconModel();
        model.type = type;
        model.hexId = hexId;
        model.base64EncodedId = base64EncodedId;
        model.status = BeaconStatus.ACTIVE.name();
        beaconModel = model;
    }

    public void onDescriptionInputted(String description) {
        beaconModel.description = description;
        getView().updateItem(beaconModel);
    }

    public void onFloorLevelInputted(String floorLevel) {
        beaconModel.floorLevel = floorLevel;
        getView().updateItem(beaconModel);
    }

    public void onStabilityInputted(String stability) {
        beaconModel.stability = stability;
        getView().updateItem(beaconModel);
    }

    public void onPlaceIdClicked() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        if (beaconModel.latLng != null) {
            builder.setLatLngBounds(new LatLngBounds(beaconModel.latLng, beaconModel.latLng));
        }
        getView().showPlacePicker(builder);
    }

    public void onPropertyInputted(String name, String value) {
        if (beaconModel.properties == null) {
            beaconModel.properties = new HashMap<>();
        }
        beaconModel.properties.put(name, value);
        getView().updateItem(beaconModel);
    }

    public void onPlaceSelected(Place place) {
        beaconModel.placeId = place.getId();
        beaconModel.latLng = place.getLatLng();

        getView().updateItem(beaconModel);
    }

    public void onPropertyRemoved(String key) {
        beaconModel.properties.remove(key);
        getView().updateItem(beaconModel);
    }

    public void onSave() {
        Subscription subscription = registerBeaconUseCase.execute(beaconModel)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .subscribe(entity -> {
                    getView().showSnackBar(R.string.message_registered);
                    getView().showBeaconRegisteredFragment();
                }, e -> {
                    LOGGER.error("Failed to save beacon", e);

                    if (e instanceof ConflictException) {
                        getView().showSnackBar(R.string.error_already_registered);
                        getView().showBeaconRegisteredFragment();
                    } else {
                        getView().showSnackBar(R.string.error_register);

                    }
                });
        subscriptions.add(subscription);
    }
}
