package com.altla.vision.beacon.manager.presentation.view;

import android.support.annotation.StringRes;

import com.altla.vision.beacon.manager.presentation.constants.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.presenter.model.AttachmentModel;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconModel;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.List;

public interface BeaconEditView {

    void showTitle(@StringRes int resId);

    void showSnackBar(@StringRes int resId);

    void updateItem(BeaconModel model);

    void updateAttachments(List<AttachmentModel> mAttachmentModels, BeaconStatus status);

    void showPlacePicker(PlacePicker.IntentBuilder builder);

    void showProgressDialog();

    void hideProgressDialog();
}
