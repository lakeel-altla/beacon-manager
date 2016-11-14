package com.altla.vision.beacon.manager.presentation.view.fragment;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.android.SnackBarUtils;
import com.altla.vision.beacon.manager.presentation.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.presenter.BeaconRegisteredPresenter;
import com.altla.vision.beacon.manager.presentation.presenter.model.RegisteredBeaconModel;
import com.altla.vision.beacon.manager.presentation.view.RegisteredBeaconView;
import com.altla.vision.beacon.manager.presentation.view.activity.MainActivity;
import com.altla.vision.beacon.manager.presentation.view.adapter.BeaconRegisteredAdapter;
import com.altla.vision.beacon.manager.presentation.view.divider.DividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeItemManagerInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class BeaconRegisteredFragment extends Fragment implements RegisteredBeaconView {

    @Inject
    BeaconRegisteredPresenter mBeaconRegisteredPresenter;

    @BindView(R.id.layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_view)
    UltimateRecyclerView mUltimateRecyclerView;

    public static BeaconRegisteredFragment newInstance() {
        BeaconRegisteredFragment fragment = new BeaconRegisteredFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_anim, container, false);
        ButterKnife.bind(this, view);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        mBeaconRegisteredPresenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        activity.setDrawerIndicatorEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mUltimateRecyclerView.setHasFixedSize(false);
        mUltimateRecyclerView.setLayoutManager(linearLayoutManager);
        mUltimateRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager
                        .findLastVisibleItemPosition();

                if (totalItemCount <= (lastVisibleItem + 1)) {
                    mBeaconRegisteredPresenter.onRefreshFromBottom();
                }
            }
        });

        BeaconRegisteredAdapter adapter = new BeaconRegisteredAdapter(mBeaconRegisteredPresenter);
        adapter.setMode(SwipeItemManagerInterface.Mode.Single);

        // 縦方向のレイアウト。
        mUltimateRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        mUltimateRecyclerView.setAdapter(adapter);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(() -> mBeaconRegisteredPresenter.onRefreshFromTop());
    }

    @Override
    public void onResume() {
        super.onResume();
        mBeaconRegisteredPresenter.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBeaconRegisteredPresenter.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showTitle(int resId) {
        getActivity().setTitle(resId);
    }

    @Override
    public void showBeaconEditFragment(String name, BeaconStatus beaconStatus) {
        ((MainActivity) getActivity()).showBeaconEditFragment(name, beaconStatus);
    }

    @Override
    public void updateItems(List<RegisteredBeaconModel> models) {
        BeaconRegisteredAdapter adapter = ((BeaconRegisteredAdapter) mUltimateRecyclerView.getAdapter());
        adapter.removeAll();
        adapter.insert(models);
    }

    @Override
    public void showSnackBar(int resId) {
        SnackBarUtils.showShort(mUltimateRecyclerView, resId);
    }

    @Override
    public void showRefreshProgress() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshProgress() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void refreshToken() {
        ((MainActivity) getActivity()).refreshToken();
    }
}
