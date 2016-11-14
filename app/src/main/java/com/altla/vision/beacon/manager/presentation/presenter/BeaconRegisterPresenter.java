package com.altla.vision.beacon.manager.presentation.presenter;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.data.exception.ConflictException;
import com.altla.vision.beacon.manager.domain.usecase.CreateAttachmentUseCase;
import com.altla.vision.beacon.manager.domain.usecase.RegisterBeaconUseCase;
import com.altla.vision.beacon.manager.domain.usecase.RemoveAttachmentUseCase;
import com.altla.vision.beacon.manager.presentation.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconModel;
import com.altla.vision.beacon.manager.presentation.view.BeaconRegisterView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class BeaconRegisterPresenter extends BasePresenter<BeaconRegisterView> implements AuthFailure {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeaconRegisterPresenter.class);

    private BeaconModel mBeaconModel;

    @Inject
    RegisterBeaconUseCase mRegisterBeaconUseCase;

    @Inject
    CreateAttachmentUseCase mCreateAttachmentUseCase;

    @Inject
    RemoveAttachmentUseCase mRemoveAttachmentUseCase;

    @Inject
    public BeaconRegisterPresenter() {
    }

    @Override
    public void onResume() {
        getView().showTitle(R.string.title_beacon_register);
        getView().updateItem(mBeaconModel);
    }

    public void setBeaconModel(String type, String hexId, String base64EncodedId) {
        BeaconModel model = new BeaconModel();
        model.mType = type;
        model.mHexId = hexId;
        model.mBase64EncodedId = base64EncodedId;
        model.mStatus = BeaconStatus.ACTIVE.name();
        mBeaconModel = model;
    }

    public void onDescriptionInputted(String description) {
        mBeaconModel.mDescription = description;
        getView().updateItem(mBeaconModel);
    }

    public void onFloorLevelInputted(String floorLevel) {
        mBeaconModel.mFloorLevel = floorLevel;
        getView().updateItem(mBeaconModel);
    }

    public void onStabilityInputted(String stability) {
        mBeaconModel.mStability = stability;
        getView().updateItem(mBeaconModel);
    }

    public void onPlaceIdClicked() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        if (mBeaconModel.mLatLng != null) {
            builder.setLatLngBounds(new LatLngBounds(mBeaconModel.mLatLng, mBeaconModel.mLatLng));
        }
        getView().showPlacePicker(builder);
    }

    public void onPropertyInputted(String name, String value) {
        if (mBeaconModel.mProperties == null) {
            mBeaconModel.mProperties = new HashMap<>();
        }
        mBeaconModel.mProperties.put(name, value);
        getView().updateItem(mBeaconModel);
    }

    public void onPlaceSelected(Place place) {
        mBeaconModel.mPlaceId = place.getId();
        mBeaconModel.mLatLng = place.getLatLng();

        getView().updateItem(mBeaconModel);
    }

    public void onPropertyRemoved(String key) {
        mBeaconModel.mProperties.remove(key);
        getView().updateItem(mBeaconModel);
    }

    public void onSave() {
        Subscription subscription = mRegisterBeaconUseCase.execute(mBeaconModel)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> getView().showProgressDialog())
                .doOnUnsubscribe(() -> getView().hideProgressDialog())
                .subscribe(entity -> {
                    getView().showSnackBar(R.string.message_registered);
                    getView().showBeaconRegisteredFragment();
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to save beacon", e);

                        if (e instanceof ConflictException) {
                            getView().showSnackBar(R.string.error_already_registered);
                            getView().showBeaconRegisteredFragment();
                        } else {
                            getView().showSnackBar(R.string.error_register);
                        }
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void refreshToken() {
        getView().refreshToken();
    }
}
