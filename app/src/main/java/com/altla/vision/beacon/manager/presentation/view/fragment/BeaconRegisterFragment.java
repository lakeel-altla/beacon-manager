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
import com.altla.vision.beacon.manager.presentation.BundleKey;
import com.altla.vision.beacon.manager.presentation.presenter.BeaconRegisterPresenter;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconModel;
import com.altla.vision.beacon.manager.presentation.view.BeaconRegisterView;
import com.altla.vision.beacon.manager.presentation.view.activity.MainActivity;
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
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class BeaconRegisterFragment extends Fragment implements BeaconRegisterView {

    @Inject
    BeaconRegisterPresenter mBeaconRegisterPresenter;

    @BindView(R.id.textView_status_description)
    TextView mStatusDescription;

    @BindView(R.id.layout)
    LinearLayout mLinearLayout;

    @BindView(R.id.propertyLayout)
    TableLayout mPropertyTableLayout;

    private static final Logger LOGGER = LoggerFactory.getLogger(BeaconRegisterFragment.class);

    private static final int REQUEST_CODE_PLACE_PICKER = 1;

    private static final int REQUEST_CODE_GOOGLE_PLAY_SERVICES_REPAIRABLE = 2;

    private Handler mHandler;

    private ProgressDialog mProgressDialog;

    private TypeLayout mTypeLayout = new TypeLayout();

    private IdLayout mIdLayout = new IdLayout();

    private StatusLayout mStatusLayout = new StatusLayout();

    private DescriptionLayout mDescriptionLayout;

    private PlaceIdLayout mPlaceIdLayout;

    private FloorLevelLayout mFloorLevelLayout;

    private StabilityLayout mStabilityLayout;

    private PropertyHeaderLayout mPropertyHeaderLayout;

    public static BeaconRegisterFragment newInstance(@NonNull String type, @NonNull String hexId, @NonNull String base64EncodedId) {

        Bundle args = new Bundle();
        args.putString(BundleKey.TYPE.name(), type);
        args.putString(BundleKey.HEX_ID.name(), hexId);
        args.putString(BundleKey.BASE64_ENCODED_ID.name(), base64EncodedId);

        BeaconRegisterFragment fragment = new BeaconRegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        setHasOptionsMenu(true);

        mDescriptionLayout = new DescriptionLayout(view ->
                new MaterialDialog.Builder(getContext())
                        .title(R.string.dialog_title_description)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input(getString(R.string.hint_description), mDescriptionLayout.mValue.getText(), (dialog, input) -> {
                            mBeaconRegisterPresenter.onDescriptionInputted(input.toString());
                        }).show()
        );

        mPlaceIdLayout = new PlaceIdLayout(view -> mBeaconRegisterPresenter.onPlaceIdClicked());

        mFloorLevelLayout = new FloorLevelLayout(view -> new MaterialDialog.Builder(getContext())
                .title(R.string.dialog_title_floor_level)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input(getActivity().getString(R.string.hint_floor_level), mFloorLevelLayout.mValue.getText(), (dialog, input) -> {
                    mBeaconRegisterPresenter.onFloorLevelInputted(input.toString());
                }).show());

        mStabilityLayout = new StabilityLayout(view ->
                new MaterialDialog.Builder(getContext())
                        .title(R.string.dialog_title_stability)
                        .items(R.array.stability)
                        .itemsCallbackSingleChoice(-1, (dialog, view1, which, input) -> {
                            mBeaconRegisterPresenter.onStabilityInputted(input.toString());
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
                        mBeaconRegisterPresenter.onPropertyInputted(name.getText().toString(), value.getText().toString());
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

        mBeaconRegisterPresenter.onCreateView(this);

        mHandler = new Handler();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        MainActivity activity = (MainActivity) getActivity();
        activity.setDrawerIndicatorEnabled(false);

        String type = getArguments().getString(BundleKey.TYPE.name());
        String hexId = getArguments().getString(BundleKey.HEX_ID.name());
        String base64EncodedId = getArguments().getString(BundleKey.BASE64_ENCODED_ID.name());
        mBeaconRegisterPresenter.setBeaconModel(type, hexId, base64EncodedId);

        mStatusDescription.setText(R.string.message_beacon_register);
        mTypeLayout.mTitle.setText(R.string.textView_type);
        mIdLayout.mTitle.setText(R.string.textView_id);
        mStatusLayout.mTitle.setText(R.string.textView_status);
        mDescriptionLayout.mTitle.setText(R.string.textView_description);
        mPlaceIdLayout.mTitle.setText(R.string.textView_place_id);
        mStabilityLayout.mTitle.setText(R.string.textView_stability);
        mFloorLevelLayout.mTitle.setText(R.string.textView_floor_level);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBeaconRegisterPresenter.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBeaconRegisterPresenter.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (REQUEST_CODE_PLACE_PICKER == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                Place place = PlacePicker.getPlace(getActivity(), intent);
                mBeaconRegisterPresenter.onPlaceSelected(place);
            } else {
                if (Activity.RESULT_CANCELED != resultCode) {
                    LOGGER.error("Failed to select a location");
                    SnackBarUtils.showShort(mLinearLayout, R.string.error_process);
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        MenuItem menuItem = menu.add(R.string.menu_title_save);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            default:
                mBeaconRegisterPresenter.onSave();
                break;
        }
        return true;
    }

    @Override
    public void showTitle(int resId) {
        getActivity().setTitle(resId);
    }

    @Override
    public void showSnackBar(int resId) {
        SnackBarUtils.showShort(mLinearLayout, resId);
    }

    @Override
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
        }
        mProgressDialog.setMessage(getContext().getResources().getString(R.string.message_progress_saving));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        mProgressDialog.dismiss();
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

        //
        // Properties
        //

        // 一度、レイアウトを全て削除。
        mPropertyTableLayout.removeAllViews();

        // ヘッダ追加。
        TableRow propertyHeader = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.property_header_item, null);
        ButterKnife.bind(mPropertyHeaderLayout, propertyHeader);
        mPropertyTableLayout.addView(propertyHeader);

        Map<String, String> map = model.mProperties;
        if (map != null) {
            // 行追加。
            for (String key : map.keySet()) {
                TableRow tableRowItem = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.property_item, null);
                tableRowItem.findViewById(R.id.imageView_remove).setOnClickListener(view -> mBeaconRegisterPresenter.onPropertyRemoved(key));

                TextView nameText = (TextView) tableRowItem.findViewById(R.id.property_name_value);
                TextView valueText = (TextView) tableRowItem.findViewById(R.id.property_value_value);

                nameText.setText(key);
                valueText.setText(map.get(key));

                mPropertyTableLayout.addView(tableRowItem);
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
    public void showBeaconRegisteredFragment() {
        ((MainActivity) getActivity()).showBeaconRegisteredFragment();
    }

    @Override
    public void refreshToken() {
        ((MainActivity) getActivity()).refreshToken();
    }
}
