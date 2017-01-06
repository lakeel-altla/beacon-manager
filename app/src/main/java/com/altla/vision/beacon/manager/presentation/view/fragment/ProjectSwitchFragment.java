package com.altla.vision.beacon.manager.presentation.view.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.presentation.presenter.ProjectSwitchPresenter;
import com.altla.vision.beacon.manager.presentation.view.SwitchProjectView;
import com.altla.vision.beacon.manager.presentation.view.activity.MainActivity;
import com.altla.vision.beacon.manager.presentation.view.adapter.ProjectSwitchAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class ProjectSwitchFragment extends Fragment implements SwitchProjectView {

    @Inject
    ProjectSwitchPresenter projectSwitchPresenter;

    @BindView(R.id.layout)
    LinearLayout linearLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.textView_current_project)
    TextView textView;

    public static ProjectSwitchFragment newInstance() {
        return new ProjectSwitchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_project_switch, container, false);
        ButterKnife.bind(this, view);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        projectSwitchPresenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        activity.setDrawerIndicatorEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        ProjectSwitchAdapter adapter = new ProjectSwitchAdapter(projectSwitchPresenter);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        projectSwitchPresenter.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        projectSwitchPresenter.onStop();
    }

    @Override
    public void refreshToken() {
        ((MainActivity) getActivity()).refreshToken();
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
    public void showCurrentProject(String projectId) {
        textView.setText(projectId);
    }

    @Override
    public void updateItems() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
