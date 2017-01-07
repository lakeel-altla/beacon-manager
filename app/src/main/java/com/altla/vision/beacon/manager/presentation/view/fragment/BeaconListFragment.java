package com.altla.vision.beacon.manager.presentation.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.presentation.constants.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.presenter.BeaconListPresenter;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconListModel;
import com.altla.vision.beacon.manager.presentation.view.BeaconListView;
import com.altla.vision.beacon.manager.presentation.view.FragmentController;
import com.altla.vision.beacon.manager.presentation.view.activity.MainActivity;
import com.altla.vision.beacon.manager.presentation.view.adapter.BeaconListAdapter;
import com.altla.vision.beacon.manager.presentation.view.divider.DividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeItemManagerInterface;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class BeaconListFragment extends Fragment implements BeaconListView {

    @Inject
    BeaconListPresenter beaconListPresenter;

    @BindView(R.id.layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    public static BeaconListFragment newInstance() {
        return new BeaconListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_anim, container, false);
        ButterKnife.bind(this, view);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        beaconListPresenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        activity.setDrawerIndicatorEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ultimateRecyclerView.setHasFixedSize(false);
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);
        ultimateRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
                    beaconListPresenter.onRefreshFromBottom();
                }
            }
        });

        BeaconListAdapter adapter = new BeaconListAdapter(beaconListPresenter);
        adapter.setMode(SwipeItemManagerInterface.Mode.Single);

        ultimateRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        ultimateRecyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> beaconListPresenter.onRefreshFromTop());
    }

    @Override
    public void onResume() {
        super.onResume();
        beaconListPresenter.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        beaconListPresenter.onStop();
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
        FragmentController controller = new FragmentController(getFragmentManager());
        controller.showBeaconEditFragment(name, beaconStatus);
    }

    @Override
    public void updateItems(List<BeaconListModel> models) {
        BeaconListAdapter adapter = ((BeaconListAdapter) ultimateRecyclerView.getAdapter());
        adapter.removeAll();
        adapter.insert(models);
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(ultimateRecyclerView, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showRefreshProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
