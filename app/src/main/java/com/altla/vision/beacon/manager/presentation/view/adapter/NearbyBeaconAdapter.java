package com.altla.vision.beacon.manager.presentation.view.adapter;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.presentation.presenter.NearbyBeaconPresenter;
import com.altla.vision.beacon.manager.presentation.presenter.model.NearbyBeaconModel;
import com.altla.vision.beacon.manager.presentation.view.NearbyBeaconItemView;

import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class NearbyBeaconAdapter extends RecyclerView.Adapter<NearbyBeaconAdapter.NearbyBeaconViewHolder> {

    private NearbyBeaconPresenter mNearbyBeaconPresenter;

    public NearbyBeaconAdapter(NearbyBeaconPresenter nearbyBeaconPresenter) {
        mNearbyBeaconPresenter = nearbyBeaconPresenter;
    }

    @Override
    public NearbyBeaconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.beacon_scanned_item, parent, false);
        NearbyBeaconViewHolder nearbyBeaconViewHolder = new NearbyBeaconViewHolder(itemView);
        mNearbyBeaconPresenter.onCreateItemView(nearbyBeaconViewHolder);
        return nearbyBeaconViewHolder;
    }

    @Override
    public void onBindViewHolder(NearbyBeaconViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mNearbyBeaconPresenter.getItemCount();
    }

    public final class NearbyBeaconViewHolder extends RecyclerView.ViewHolder implements NearbyBeaconItemView {

        @BindView(R.id.layout_row)
        LinearLayout mLinearLayout;

        @BindView(R.id.textView_id)
        TextView mTextViewId;

        @BindView(R.id.textView_type)
        TextView mTextViewType;

        private NearbyBeaconPresenter.NearbyBeaconItemPresenter mNearbyBeaconItemPresenter;

        public NearbyBeaconViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setItemPresenter(NearbyBeaconPresenter.NearbyBeaconItemPresenter itemPresenter) {
            mNearbyBeaconItemPresenter = itemPresenter;
        }

        @Override
        public void showItem(NearbyBeaconModel model) {
            mTextViewId.setText(model.mHexId);
            mTextViewType.setText(model.mType.getValue());
            mLinearLayout.setOnClickListener(view -> mNearbyBeaconItemPresenter.onItemClick(model));
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            mNearbyBeaconItemPresenter.onBind(position);
        }
    }
}
