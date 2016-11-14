package com.altla.vision.beacon.manager.presentation.view.adapter;

import com.altla.vision.beacon.manager.presentation.BeaconStatus;
import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.presentation.presenter.BeaconRegisteredPresenter;
import com.altla.vision.beacon.manager.presentation.presenter.model.RegisteredBeaconModel;
import com.altla.vision.beacon.manager.presentation.view.BeaconRegisteredItemView;
import com.marshalchen.ultimaterecyclerview.SwipeableUltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeLayout;

import android.support.annotation.IntRange;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class BeaconRegisteredAdapter extends SwipeableUltimateViewAdapter<RegisteredBeaconModel> {

    private BeaconRegisteredPresenter mBeaconRegisteredPresenter;

    public BeaconRegisteredAdapter(BeaconRegisteredPresenter presenter) {
        super(new ArrayList<>());
        mBeaconRegisteredPresenter = presenter;
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.beacon_registered_item;
    }

    @Override
    protected UltimateRecyclerviewViewHolder newViewHolder(View view) {
        RegisteredBeaconViewHolder registeredBeaconViewHolder = new RegisteredBeaconViewHolder(view, true);
        mBeaconRegisteredPresenter.onCreateItemView(registeredBeaconViewHolder);
        return registeredBeaconViewHolder;
    }

    @Override
    protected void withBindHolder(UltimateRecyclerviewViewHolder bindHolder, RegisteredBeaconModel model, int position) {
        RegisteredBeaconViewHolder holder = (RegisteredBeaconViewHolder) bindHolder;
        holder.onBind(position);
    }

    public final class RegisteredBeaconViewHolder extends UltimateRecyclerviewViewHolder implements BeaconRegisteredItemView {

        @BindView(R.id.layout_row)
        LinearLayout mRowLayout;

        @BindView(R.id.textView_id)
        TextView mId;

        @BindView(R.id.textView_type)
        TextView mType;

        @BindView(R.id.textView_status)
        TextView mStatus;

        @BindView(R.id.textView_description)
        TextView mDescription;

        @BindView(R.id.swipe_layout)
        SwipeLayout mSwipeLayout;

        @BindView(R.id.button_state)
        Button mStateButton;

        @BindView(R.id.button_decommission)
        Button mDecommissionButton;

        private BeaconRegisteredPresenter.BeaconRegisteredItemPresenter mItemPresenter;

        public RegisteredBeaconViewHolder(View itemView, boolean bind) {
            super(itemView);
            if (bind) {
                ButterKnife.bind(this, itemView);
                mSwipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);
                mSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            }
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            mItemPresenter.onBind(position);
        }

        @Override
        public void setItemPresenter(BeaconRegisteredPresenter.BeaconRegisteredItemPresenter beaconRegisteredItemPresenter) {
            mItemPresenter = beaconRegisteredItemPresenter;
        }

        @Override
        public void updateItem(RegisteredBeaconModel model) {
            mId.setText(model.mHexId);
            mType.setText(model.mType);
            mDescription.setText(model.mDescription);
            mStatus.setText(model.mStatus);
            mRowLayout.setOnClickListener(view -> mItemPresenter.onClick(model.mName, model.mStatus));
            mDecommissionButton.setOnClickListener(view -> mItemPresenter.onDecommissionButtonClicked(model));

            if (BeaconStatus.ACTIVE.name().equals(model.mStatus)) {
                mStateButton.setText(R.string.button_deactivate);
                mStateButton.setOnClickListener(view -> mItemPresenter.onDeactivateButtonClicked(model));
            } else if (BeaconStatus.INACTIVE.name().equals(model.mStatus)) {
                mStateButton.setText(R.string.button_activate);
                mStateButton.setOnClickListener(view -> mItemPresenter.onActivateButtonClicked(model));
            } else {
                mSwipeLayout.clearDragEdge();
            }
        }
    }
}
