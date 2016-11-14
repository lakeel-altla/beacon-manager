package com.altla.vision.beacon.manager.presentation.view;

import com.google.android.gms.location.places.ui.PlacePicker;

import com.altla.vision.beacon.manager.presentation.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.presenter.model.AttachmentModel;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconModel;

import android.support.annotation.StringRes;

import java.util.List;

public interface BeaconEditView extends AuthView {

    void showTitle(@StringRes int resId);

    void showSnackBar(@StringRes int resId);

    void updateItem(BeaconModel model);

    void updateAttachments(List<AttachmentModel> mAttachmentModels, BeaconStatus status);

    void showPlacePicker(PlacePicker.IntentBuilder builder);

    void showProgressDialog();

    void hideProgressDialog();
}
