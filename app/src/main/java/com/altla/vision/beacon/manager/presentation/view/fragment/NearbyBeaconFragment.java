package com.altla.vision.beacon.manager.presentation.view.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.android.SnackBarUtils;
import com.altla.vision.beacon.manager.presentation.presenter.NearbyBeaconPresenter;
import com.altla.vision.beacon.manager.presentation.view.NearbyBeaconView;
import com.altla.vision.beacon.manager.presentation.view.activity.MainActivity;
import com.altla.vision.beacon.manager.presentation.view.adapter.NearbyBeaconAdapter;
import com.altla.vision.beacon.manager.presentation.view.divider.DividerItemDecoration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class NearbyBeaconFragment extends Fragment implements NearbyBeaconView {

    @Inject
    NearbyBeaconPresenter mNearbyBeaconPresenter;

    @BindView(R.id.layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private static final Logger LOGGER = LoggerFactory.getLogger(NearbyBeaconFragment.class);

    private static final int REQUEST_CODE_ENABLE_BLE = 1;

    private static final int REQUEST_CODE_ACCESS_FINE_LOCATION = 2;

    // デフォルトコンストラクタは残しておかなければならない。
    public static NearbyBeaconFragment newInstance() {
        return new NearbyBeaconFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        mNearbyBeaconPresenter.onCreateView(this);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mNearbyBeaconPresenter.checkBleEnabled(getActivity());

        MainActivity activity = (MainActivity) getActivity();
        activity.setDrawerIndicatorEnabled(true);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 500);
            mNearbyBeaconPresenter.onRefresh();
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        NearbyBeaconAdapter nearbyBeaconAdapter = new NearbyBeaconAdapter(mNearbyBeaconPresenter);
        mRecyclerView.setAdapter(nearbyBeaconAdapter);

        int permissionResult = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionResult != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mNearbyBeaconPresenter.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mNearbyBeaconPresenter.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_ENABLE_BLE:
                mNearbyBeaconPresenter.setBeaconManager();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // TODO: Android 6 系の Permission 対応。
        if (requestCode != REQUEST_CODE_ACCESS_FINE_LOCATION) {
            LOGGER.warn("Permission denied");
        }
    }

    @Override
    public void showTitle(int resId) {
        getActivity().setTitle(resId);
    }

    @Override
    public void showBleEnabledActivity() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BLE);
    }

    @Override
    public void updateItems() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void removeAllItems(int size) {
        mRecyclerView.getAdapter().notifyItemRangeRemoved(0, size);
    }

    @Override
    public void showSnackBar(int resId) {
        SnackBarUtils.showShort(mRecyclerView, resId);
    }

    @Override
    public void showBeaconRegisterFragment(String type, String hexId, String base64EncodedId) {
        ((MainActivity) getActivity()).showBeaconRegisterFragment(type, hexId, base64EncodedId);
    }

    @Override
    public void refreshToken() {
        ((MainActivity) getActivity()).refreshToken();
    }
}
