package com.altla.vision.beacon.manager.presentation.view.fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import com.afollestad.materialdialogs.MaterialDialog;
import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.android.SnackBarUtils;
import com.altla.vision.beacon.manager.core.CollectionUtils;
import com.altla.vision.beacon.manager.presentation.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.BundleKey;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BeaconEditFragment extends Fragment implements BeaconEditView {

    @Inject
    BeaconEditPresenter mBeaconEditPresenter;

    @BindView(R.id.textView_status_description)
    TextView mStatusDescription;

    @BindView(R.id.layout)
    LinearLayout mLinearLayout;

    @BindView(R.id.propertyLayout)
    TableLayout mPropertyTableLayout;

    @BindView(R.id.attachmentLayout)
    TableLayout mAttachmentTableLayout;

    private static final Logger LOGGER = LoggerFactory.getLogger(BeaconEditFragment.class);

    private static final int REQUEST_CODE_PLACE_PICKER = 1;

    private static final int REQUEST_CODE_GOOGLE_PLAY_SERVICES_REPAIRABLE = 2;

    private ProgressDialog mProgressDialog;

    private TypeLayout mTypeLayout = new TypeLayout();

    private IdLayout mIdLayout = new IdLayout();

    private StatusLayout mStatusLayout = new StatusLayout();

    private DescriptionLayout mDescriptionLayout;

    private PlaceIdLayout mPlaceIdLayout;

    private FloorLevelLayout mFloorLevelLayout;

    private StabilityLayout mStabilityLayout;

    private PropertyHeaderLayout mPropertyHeaderLayout;

    private AttachmentHeaderLayout mAttachmentHeaderLayout;

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

        // 呼び出さなければ、onOptionsItemSelected() が呼ばれない。
        setHasOptionsMenu(true);

        // 各種設定項目のレイアウトの初期化。

        mDescriptionLayout = new DescriptionLayout(view ->
                new MaterialDialog.Builder(getContext())
                        .title(R.string.dialog_title_description)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input(getString(R.string.hint_description), mDescriptionLayout.mValue.getText(), (dialog, input) -> {
                            mBeaconEditPresenter.onDescriptionInputted(input.toString());
                        }).show()
        );

        mPlaceIdLayout = new PlaceIdLayout(view -> mBeaconEditPresenter.onPlaceIdClicked());

        mFloorLevelLayout = new FloorLevelLayout(view ->
                new MaterialDialog.Builder(getContext())
                        .title(R.string.dialog_title_floor_level)
                        .inputType(InputType.TYPE_CLASS_NUMBER)
                        .input(getActivity().getString(R.string.hint_floor_level), mFloorLevelLayout.mValue.getText(), (dialog, input) -> {
                            mBeaconEditPresenter.onFloorLevelInputted(input.toString());
                        }).show());

        mStabilityLayout = new StabilityLayout(view ->
                new MaterialDialog.Builder(getContext())
                        .title(R.string.dialog_title_stability)
                        .items(R.array.stability)
                        .itemsCallbackSingleChoice(-1, (dialog, view1, which, input) -> {
                            mBeaconEditPresenter.onStabilityInputted(input.toString());
                            return true;
                        })
                        .positiveText(R.string.dialog_ok)
                        .show()
        );

        mPropertyHeaderLayout = new PropertyHeaderLayout(view -> {
            View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_property, null);
            new MaterialDialog.Builder(getContext())
                    .title(R.string.dialog_title_property)
                    .customView(dialogView, true)
                    .positiveText(R.string.dialog_ok)
                    .onPositive((dialog, which) -> {
                        dialog.dismiss();
                        EditText name = (EditText) dialogView.findViewById(R.id.editText_name);
                        EditText value = (EditText) dialogView.findViewById(R.id.editText_value);
                        mBeaconEditPresenter.onPropertyInputted(name.getText().toString(), value.getText().toString());
                    })
                    .show();

        });

        mAttachmentHeaderLayout = new AttachmentHeaderLayout(view -> {
            View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_attachment, null);
            new MaterialDialog.Builder(getContext())
                    .title(R.string.dialog_title_attachment)
                    .customView(dialogView, true)
                    .positiveText(R.string.dialog_ok)
                    .onPositive((dialog, which) -> {
                        dialog.dismiss();
                        EditText type = (EditText) dialogView.findViewById(R.id.editText_type);
                        EditText data = (EditText) dialogView.findViewById(R.id.editText_data);
                        mBeaconEditPresenter.onAttachmentInputted(type.getText().toString(), data.getText().toString());
                    })
                    .show();
        });

        View view = inflater.inflate(R.layout.fragment_beacon_settings, container, false);
        ButterKnife.bind(this, view);
        ButterKnife.bind(mTypeLayout, view.findViewById(R.id.type));
        ButterKnife.bind(mIdLayout, view.findViewById(R.id.id));
        ButterKnife.bind(mStatusLayout, view.findViewById(R.id.status));
        ButterKnife.bind(mDescriptionLayout, view.findViewById(R.id.description));
        ButterKnife.bind(mPlaceIdLayout, view.findViewById(R.id.place));
        ButterKnife.bind(mStabilityLayout, view.findViewById(R.id.stability));
        ButterKnife.bind(mFloorLevelLayout, view.findViewById(R.id.floorLevel));

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        mBeaconEditPresenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        mTypeLayout.mTitle.setText(R.string.textView_type);
        mIdLayout.mTitle.setText(R.string.textView_id);
        mStatusLayout.mTitle.setText(R.string.textView_status);
        mDescriptionLayout.mTitle.setText(R.string.textView_description);
        mPlaceIdLayout.mTitle.setText(R.string.textView_place_id);
        mStabilityLayout.mTitle.setText(R.string.textView_stability);
        mFloorLevelLayout.mTitle.setText(R.string.textView_floor_level);

        MainActivity activity = (MainActivity) getActivity();
        activity.setDrawerIndicatorEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBeaconEditPresenter.onResume();

        BeaconStatus beaconStatus = BeaconStatus.toStatus((String) getArguments().get(BundleKey.STATUS.name()));
        switch (beaconStatus) {
            case ACTIVE:
                mStatusDescription.setText(R.string.message_beacon_activate);
                break;
            case INACTIVE:
                mStatusDescription.setText(R.string.message_beacon_deactivate);
                break;
            case DECOMMISSIONED:
                mDescriptionLayout.setClickable(false);
                mPlaceIdLayout.setClickable(false);
                mFloorLevelLayout.setClickable(false);
                mStabilityLayout.setClickable(false);
                mStatusDescription.setText(R.string.message_beacon_decommissioned);
                break;
            default:
                LOGGER.warn("Unknown beacon beaconStatus:beaconStatus=", beaconStatus.getValue());
                break;
        }

        String beaconName = (String) getArguments().get(BundleKey.NAME.name());
        mBeaconEditPresenter.setBeaconModel(beaconName);
        mBeaconEditPresenter.findBeacon();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBeaconEditPresenter.onStop();
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
                mBeaconEditPresenter.onSelectedPlace(place);
            } else {
                if (Activity.RESULT_CANCELED != resultCode) {
                    LOGGER.error("Failed to select a location");
                    SnackBarUtils.showShort(mLinearLayout, R.string.error_process);
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
        SnackBarUtils.showLong(mLinearLayout, resId);
    }

    @Override
    public void updateItem(BeaconModel model) {
        mTypeLayout.mValue.setText(model.mType);
        mIdLayout.mValue.setText(model.mHexId);
        mStatusLayout.mValue.setText(model.mStatus);
        mDescriptionLayout.mValue.setText(model.mDescription);
        mPlaceIdLayout.mValue.setText(model.mPlaceId);
        mFloorLevelLayout.mValue.setText(model.mFloorLevel);
        mStabilityLayout.mValue.setText(model.mStability);

        boolean isClickable = BeaconStatus.DECOMMISSIONED != BeaconStatus.toStatus(model.mStatus);

        //
        // Properties
        //

        // 一度、全ての View を削除。
        mPropertyTableLayout.removeAllViews();

        // ヘッダ追加。
        View tableRowHeaderView = getActivity().getLayoutInflater().inflate(R.layout.property_header_item, null);
        ButterKnife.bind(mPropertyHeaderLayout, tableRowHeaderView);

        mPropertyHeaderLayout.setClickable(isClickable);
        mPropertyTableLayout.addView(tableRowHeaderView);

        Map<String, String> map = model.mProperties;
        if (map != null) {
            // 行追加。
            for (String key : map.keySet()) {
                TableRow tableRowItem = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.property_item, null);
                TextView nameText = (TextView) tableRowItem.findViewById(R.id.property_name_value);
                TextView valueText = (TextView) tableRowItem.findViewById(R.id.property_value_value);

                nameText.setText(key);
                valueText.setText(map.get(key));

                ImageView removeButton = (ImageView) tableRowItem.findViewById(R.id.imageView_remove);
                if (BeaconStatus.DECOMMISSIONED == BeaconStatus.toStatus(model.mStatus)) {
                    removeButton.setVisibility(View.GONE);
                } else {
                    removeButton.setOnClickListener(view -> mBeaconEditPresenter.onPropertyRemoved(key));
                }

                mPropertyTableLayout.addView(tableRowItem);
            }
        }
    }

    @Override
    public void updateAttachments(List<AttachmentModel> attachmentModels, BeaconStatus status) {
        //
        // Attachments
        //

        // 一度、レイアウトを全て削除。
        mAttachmentTableLayout.removeAllViews();

        // ヘッダ追加。
        View attachmentHeaderView = getActivity().getLayoutInflater().inflate(R.layout.attachment_header_item, null);
        ButterKnife.bind(mAttachmentHeaderLayout, attachmentHeaderView);

        boolean isClickable = BeaconStatus.DECOMMISSIONED != status;
        mAttachmentHeaderLayout.setClickable(isClickable);
        mAttachmentTableLayout.addView(attachmentHeaderView);

        if (!CollectionUtils.isEmpty(attachmentModels)) {
            // 行追加。
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
                        removeButton.setOnClickListener(view -> mBeaconEditPresenter.onAttachmentRemoved(attachmentModel));
                        break;
                }

                TextView typeText = (TextView) tableRowItem.findViewById(R.id.attachment_type_value);
                TextView dataText = (TextView) tableRowItem.findViewById(R.id.attachment_data_value);

                typeText.setText(attachmentModel.mType);
                dataText.setText(attachmentModel.mData);

                mAttachmentTableLayout.addView(tableRowItem);
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
                SnackBarUtils.showShort(mLinearLayout, R.string.error_google_play_services_unavailable);
            }
        }
    }

    @Override
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
        }
        mProgressDialog.setMessage(getContext().getResources().getString(R.string.message_progress_saving));
        mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        mProgressDialog.hide();
    }

    @Override
    public void refreshToken() {
        ((MainActivity) getActivity()).refreshToken();
    }
}
