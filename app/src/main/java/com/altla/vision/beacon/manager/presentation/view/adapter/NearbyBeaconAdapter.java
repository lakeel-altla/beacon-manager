package com.altla.vision.beacon.manager.presentation.view.adapter;

import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.presentation.presenter.NearbyBeaconPresenter;
import com.altla.vision.beacon.manager.presentation.presenter.model.NearbyBeaconModel;
import com.altla.vision.beacon.manager.presentation.view.NearbyBeaconItemView;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class NearbyBeaconAdapter extends RecyclerView.Adapter<NearbyBeaconAdapter.NearbyBeaconViewHolder> {

    private NearbyBeaconPresenter nearbyBeaconPresenter;

    public NearbyBeaconAdapter(NearbyBeaconPresenter nearbyBeaconPresenter) {
        this.nearbyBeaconPresenter = nearbyBeaconPresenter;
    }

    @Override
    public NearbyBeaconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.beacon_scanned_item, parent, false);
        NearbyBeaconViewHolder nearbyBeaconViewHolder = new NearbyBeaconViewHolder(itemView);
        nearbyBeaconPresenter.onCreateItemView(nearbyBeaconViewHolder);
        return nearbyBeaconViewHolder;
    }

    @Override
    public void onBindViewHolder(NearbyBeaconViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return nearbyBeaconPresenter.getItemCount();
    }

    public final class NearbyBeaconViewHolder extends RecyclerView.ViewHolder implements NearbyBeaconItemView {

        @BindView(R.id.layout_row)
        LinearLayout linearLayout;

        @BindView(R.id.textView_id)
        TextView textView;

        @BindView(R.id.textView_type)
        TextView textViewType;

        private NearbyBeaconPresenter.NearbyBeaconItemPresenter nearbyBeaconItemPresenter;

        NearbyBeaconViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setItemPresenter(NearbyBeaconPresenter.NearbyBeaconItemPresenter itemPresenter) {
            nearbyBeaconItemPresenter = itemPresenter;
        }

        @Override
        public void showItem(NearbyBeaconModel model) {
            textView.setText(model.hexId);
            textViewType.setText(model.type.getValue());
            linearLayout.setOnClickListener(view -> nearbyBeaconItemPresenter.onItemClick(model));
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            nearbyBeaconItemPresenter.onBind(position);
        }
    }
}
