package com.altla.vision.beacon.manager.presentation.view.adapter;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.presentation.presenter.ProjectSwitchPresenter;
import com.altla.vision.beacon.manager.presentation.presenter.model.NameSpaceModel;
import com.altla.vision.beacon.manager.presentation.view.ProjectSwitchItemView;

import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class ProjectSwitchAdapter extends RecyclerView.Adapter<ProjectSwitchAdapter.ProjectSwitchViewHolder> {

    private ProjectSwitchPresenter mProjectSwitchPresenter;

    public ProjectSwitchAdapter(ProjectSwitchPresenter projectSwitchPresenter) {
        mProjectSwitchPresenter = projectSwitchPresenter;
    }

    @Override
    public ProjectSwitchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.namespace_item, parent, false);
        ProjectSwitchViewHolder projectSwitchViewHolder = new ProjectSwitchViewHolder(itemView);
        mProjectSwitchPresenter.onCreateItemView(projectSwitchViewHolder);
        return projectSwitchViewHolder;
    }

    @Override
    public void onBindViewHolder(ProjectSwitchViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mProjectSwitchPresenter.getItemCount();
    }

    public final class ProjectSwitchViewHolder extends RecyclerView.ViewHolder implements ProjectSwitchItemView {

        @BindView(R.id.layout_row)
        LinearLayout mLinearLayout;

        @BindView(R.id.textView_project_id)
        TextView mProjectId;

        private ProjectSwitchPresenter.ProjectSwitchItemPresenter mItemPresenter;

        public ProjectSwitchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setItemPresenter(ProjectSwitchPresenter.ProjectSwitchItemPresenter itemPresenter) {
            mItemPresenter = itemPresenter;
        }

        @Override
        public void showItem(NameSpaceModel model) {
            mProjectId.setText(model.mProjectId);
            mLinearLayout.setOnClickListener(view -> mItemPresenter.onItemClick(model));
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            mItemPresenter.onBind(position);
        }
    }
}
