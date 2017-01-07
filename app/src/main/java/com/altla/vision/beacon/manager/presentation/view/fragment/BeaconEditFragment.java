package com.altla.vision.beacon.manager.presentation.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.core.CollectionUtils;
import com.altla.vision.beacon.manager.presentation.constants.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.constants.BundleKey;
import com.altla.vision.beacon.manager.presentation.presenter.BeaconEditPresenter;
import com.altla.vision.beacon.manager.presentation.presenter.model.AttachmentModel;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconModel;
import com.altla.vision.beacon.manager.presentation.view.BeaconEditView;
import com.altla.vision.beacon.manager.presentation.view.activity.MainActivity;
import com.altla.vision.beacon.manager.presentation.view.layout.AttachmentHeaderLayout;
import com.altla.vision.beacon.manager.presentation.view.layout.DescriptionLayout;
import com.altla.vision.beacon.manager.presentation.view.layout.FloorLevelLayout;
import com.altla.vision.beacon.manager.presentation.view.layout.IdLayout;
import com.altla.vision.beacon.manager.presentation.view.layout.PlaceIdLayout;
import com.altla.vision.beacon.manager.presentation.view.layout.PropertyHeaderLayout;
import com.altla.vision.beacon.manager.presentation.view.layout.StabilityLayout;
import com.altla.vision.beacon.manager.presentation.view.layout.StatusLayout;
import com.altla.vision.beacon.manager.presentation.view.layout.TypeLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BeaconEditFragment extends Fragment implements BeaconEditView {

    @Inject
    BeaconEditPresenter beaconEditPresenter;

    @BindView(R.id.textView_status_description)
    TextView statusDescription;

    @BindView(R.id.layout)
    LinearLayout linearLayout;

    @BindView(R.id.propertyLayout)
    TableLayout propertyTableLayout;

    @BindView(R.id.attachmentLayout)
    TableLayout attachmentTableLayout;

    private static final Logger LOGGER = LoggerFactory.getLogger(BeaconEditFragment.class);

    private static final int REQUEST_CODE_PLACE_PICKER = 1;

    private static final int REQUEST_CODE_GOOGLE_PLAY_SERVICES_REPAIRABLE = 2;

    private ProgressDialog progressDialog;

    private TypeLayout typeLayout = new TypeLayout();

    private IdLayout idLayout = new IdLayout();

    private StatusLayout statusLayout = new StatusLayout();

    private DescriptionLayout descriptionLayout;

    private PlaceIdLayout placeIdLayout;

    private FloorLevelLayout floorLevelLayout;

    private StabilityLayout stabilityLayout;

    private PropertyHeaderLayout propertyHeaderLayout;

    private AttachmentHeaderLayout attachmentHeaderLayout;

    public static BeaconEditFragment newInstance(String name, BeaconStatus beaconStatus) {

        Bundle args = new Bundle();
        args.putString(BundleKey.NAME.name(), name);
        args.putString(BundleKey.STATUS.name(), beaconStatus.getValue());

        BeaconEditFragment fragment = new BeaconEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        descriptionLayout = new DescriptionLayout(view ->
                new MaterialDialog.Builder(getContext())
                        .title(R.string.dialog_title_description)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input(getString(R.string.hint_description), descriptionLayout.value.getText(), (dialog, input) -> {
                            beaconEditPresenter.onDescriptionInputted(input.toString());
                        }).show()
        );

        placeIdLayout = new PlaceIdLayout(view -> beaconEditPresenter.onPlaceIdClicked());

        floorLevelLayout = new FloorLevelLayout(view ->
                new MaterialDialog.Builder(getContext())
                        .title(R.string.dialog_title_floor_level)
                        .inputType(InputType.TYPE_CLASS_NUMBER)
                        .input(getActivity().getString(R.string.hint_floor_level), floorLevelLayout.value.getText(), (dialog, input) -> {
                            beaconEditPresenter.onFloorLevelInputted(input.toString());
                        }).show());

        stabilityLayout = new StabilityLayout(view ->
                new MaterialDialog.Builder(getContext())
                        .title(R.string.dialog_title_stability)
                        .items(R.array.stability)
                        .itemsCallbackSingleChoice(-1, (dialog, view1, which, input) -> {
                            beaconEditPresenter.onStabilityInputted(input.toString());
                            return true;
                        })
                        .positiveText(R.string.dialog_ok)
                        .show()
        );

        propertyHeaderLayout = new PropertyHeaderLayout(view -> {
            View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_property, null);
            new MaterialDialog.Builder(getContext())
                    .title(R.string.dialog_title_property)
                    .customView(dialogView, true)
                    .positiveText(R.string.dialog_ok)
                    .onPositive((dialog, which) -> {
                        dialog.dismiss();
                        EditText name = (EditText) dialogView.findViewById(R.id.editText_name);
                        EditText value = (EditText) dialogView.findViewById(R.id.editText_value);
                        beaconEditPresenter.onPropertyInputted(name.getText().toString(), value.getText().toString());
                    })
                    .show();

        });

        attachmentHeaderLayout = new AttachmentHeaderLayout(view -> {
            View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_attachment, null);
            new MaterialDialog.Builder(getContext())
                    .title(R.string.dialog_title_attachment)
                    .customView(dialogView, true)
                    .positiveText(R.string.dialog_ok)
                    .onPositive((dialog, which) -> {
                        dialog.dismiss();
                        EditText type = (EditText) dialogView.findViewById(R.id.editText_type);
                        EditText data = (EditText) dialogView.findViewById(R.id.editText_data);
                        beaconEditPresenter.onAttachmentInputted(type.getText().toString(), data.getText().toString());
                    })
                    .show();
        });

        View view = inflater.inflate(R.layout.fragment_beacon_settings, container, false);
        ButterKnife.bind(this, view);
        ButterKnife.bind(typeLayout, view.findViewById(R.id.type));
        ButterKnife.bind(idLayout, view.findViewById(R.id.id));
        ButterKnife.bind(statusLayout, view.findViewById(R.id.status));
        ButterKnife.bind(descriptionLayout, view.findViewById(R.id.description));
        ButterKnife.bind(placeIdLayout, view.findViewById(R.id.place));
        ButterKnife.bind(stabilityLayout, view.findViewById(R.id.stability));
        ButterKnife.bind(floorLevelLayout, view.findViewById(R.id.floorLevel));

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        beaconEditPresenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        typeLayout.title.setText(R.string.textView_type);
        idLayout.title.setText(R.string.textView_id);
        statusLayout.title.setText(R.string.textView_status);
        descriptionLayout.title.setText(R.string.textView_description);
        placeIdLayout.title.setText(R.string.textView_place_id);
        stabilityLayout.title.setText(R.string.textView_stability);
        floorLevelLayout.title.setText(R.string.textView_floor_level);

        MainActivity activity = (MainActivity) getActivity();
        activity.setDrawerIndicatorEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        beaconEditPresenter.onResume();

        BeaconStatus beaconStatus = BeaconStatus.toStatus((String) getArguments().get(BundleKey.STATUS.name()));
        switch (beaconStatus) {
            case ACTIVE:
                statusDescription.setText(R.string.message_beacon_activate);
                break;
            case INACTIVE:
                statusDescription.setText(R.string.message_beacon_deactivate);
                break;
            case DECOMMISSIONED:
                descriptionLayout.setClickable(false);
                placeIdLayout.setClickable(false);
                floorLevelLayout.setClickable(false);
                stabilityLayout.setClickable(false);
                statusDescription.setText(R.string.message_beacon_decommissioned);
                break;
            default:
                LOGGER.warn("Unknown beacon beaconStatus:beaconStatus=", beaconStatus.getValue());
                break;
        }

        String beaconName = (String) getArguments().get(BundleKey.NAME.name());
        beaconEditPresenter.setBeaconModel(beaconName);
        beaconEditPresenter.findBeacon();
    }

    @Override
    public void onStop() {
        super.onStop();
        beaconEditPresenter.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (REQUEST_CODE_PLACE_PICKER == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                Place place = PlacePicker.getPlace(getActivity(), intent);
                beaconEditPresenter.onSelectedPlace(place);
            } else {
                if (Activity.RESULT_CANCELED != resultCode) {
                    LOGGER.error("Failed to select a location");
                    Snackbar.make(linearLayout, R.string.error_process, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void showTitle(int resId) {
        getActivity().setTitle(resId);
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(linearLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void updateItem(BeaconModel model) {
        typeLayout.value.setText(model.type);
        idLayout.value.setText(model.hexId);
        statusLayout.value.setText(model.status);
        descriptionLayout.value.setText(model.description);
        placeIdLayout.value.setText(model.placeId);
        floorLevelLayout.value.setText(model.floorLevel);
        stabilityLayout.value.setText(model.stability);

        boolean isClickable = BeaconStatus.DECOMMISSIONED != BeaconStatus.toStatus(model.status);

        //
        // Properties
        //

        // Remove all views.
        propertyTableLayout.removeAllViews();

        // Add a header view.
        View tableRowHeaderView = getActivity().getLayoutInflater().inflate(R.layout.property_header_item, null);
        ButterKnife.bind(propertyHeaderLayout, tableRowHeaderView);

        propertyHeaderLayout.setClickable(isClickable);
        propertyTableLayout.addView(tableRowHeaderView);

        Map<String, String> map = model.properties;
        if (map != null) {
            // Add rows.
            for (String key : map.keySet()) {
                TableRow tableRowItem = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.property_item, null);
                TextView nameText = (TextView) tableRowItem.findViewById(R.id.property_name_value);
                TextView valueText = (TextView) tableRowItem.findViewById(R.id.property_value_value);

                nameText.setText(key);
                valueText.setText(map.get(key));

                ImageView removeButton = (ImageView) tableRowItem.findViewById(R.id.imageView_remove);
                if (BeaconStatus.DECOMMISSIONED == BeaconStatus.toStatus(model.status)) {
                    removeButton.setVisibility(View.GONE);
                } else {
                    removeButton.setOnClickListener(view -> beaconEditPresenter.onPropertyRemoved(key));
                }

                propertyTableLayout.addView(tableRowItem);
            }
        }
    }

    @Override
    public void updateAttachments(List<AttachmentModel> attachmentModels, BeaconStatus status) {
        //
        // Attachments
        //

        // Remove all views.
        attachmentTableLayout.removeAllViews();

        // Add a header view.
        View attachmentHeaderView = getActivity().getLayoutInflater().inflate(R.layout.attachment_header_item, null);
        ButterKnife.bind(attachmentHeaderLayout, attachmentHeaderView);

        boolean isClickable = BeaconStatus.DECOMMISSIONED != status;
        attachmentHeaderLayout.setClickable(isClickable);
        attachmentTableLayout.addView(attachmentHeaderView);

        if (!CollectionUtils.isEmpty(attachmentModels)) {
            // Add a row.
            for (AttachmentModel attachmentModel : attachmentModels) {
                TableRow tableRowItem = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.attachment_item, null);

                ImageView removeButton = (ImageView) tableRowItem.findViewById(R.id.imageView_remove);
                switch (status) {
                    case DECOMMISSIONED:
                        removeButton.setVisibility(View.GONE);
                        break;
                    case UNKNOWN:
                    case ACTIVE:
                    case INACTIVE:
                    default:
                        removeButton.setOnClickListener(view -> beaconEditPresenter.onAttachmentRemoved(attachmentModel));
                        break;
                }

                TextView typeText = (TextView) tableRowItem.findViewById(R.id.attachment_type_value);
                TextView dataText = (TextView) tableRowItem.findViewById(R.id.attachment_data_value);

                typeText.setText(attachmentModel.type);
                dataText.setText(attachmentModel.data);

                attachmentTableLayout.addView(tableRowItem);
            }
        }
    }

    @Override
    public void showPlacePicker(PlacePicker.IntentBuilder builder) {
        try {
            startActivityForResult(builder.build(getActivity()), REQUEST_CODE_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException e) {
            LOGGER.error("Google play services not installed", e);
            int statusCode = e.getConnectionStatusCode();
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), statusCode, REQUEST_CODE_GOOGLE_PLAY_SERVICES_REPAIRABLE);
        } catch (GooglePlayServicesNotAvailableException e) {
            LOGGER.error("Google play services not availability", e);
            if (ConnectionResult.SUCCESS != e.errorCode) {
                Snackbar.make(linearLayout, R.string.error_google_play_services_unavailable, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
        }
        progressDialog.setMessage(getContext().getResources().getString(R.string.message_progress_saving));
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.hide();
    }

    @Override
    public void refreshToken() {
        ((MainActivity) getActivity()).refreshToken();
    }
}
