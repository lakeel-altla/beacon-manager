package com.altla.vision.beacon.manager.presentation.view.adapter;

import android.support.annotation.IntRange;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.presentation.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.presenter.BeaconRegisteredPresenter;
import com.altla.vision.beacon.manager.presentation.presenter.model.RegisteredBeaconModel;
import com.altla.vision.beacon.manager.presentation.view.BeaconRegisteredItemView;
import com.marshalchen.ultimaterecyclerview.SwipeableUltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class BeaconRegisteredAdapter extends SwipeableUltimateViewAdapter<RegisteredBeaconModel> {

    private BeaconRegisteredPresenter beaconRegisteredPresenter;

    public BeaconRegisteredAdapter(BeaconRegisteredPresenter presenter) {
        super(new ArrayList<>());
        beaconRegisteredPresenter = presenter;
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.beacon_registered_item;
    }

    @Override
    protected UltimateRecyclerviewViewHolder newViewHolder(View view) {
        RegisteredBeaconViewHolder registeredBeaconViewHolder = new RegisteredBeaconViewHolder(view, true);
        beaconRegisteredPresenter.onCreateItemView(registeredBeaconViewHolder);
        return registeredBeaconViewHolder;
    }

    @Override
    protected void withBindHolder(UltimateRecyclerviewViewHolder bindHolder, RegisteredBeaconModel model, int position) {
        RegisteredBeaconViewHolder holder = (RegisteredBeaconViewHolder) bindHolder;
        holder.onBind(position);
    }

    final class RegisteredBeaconViewHolder extends UltimateRecyclerviewViewHolder implements BeaconRegisteredItemView {

        @BindView(R.id.layout_row)
        LinearLayout rowLayout;

        @BindView(R.id.textView_id)
        TextView id;

        @BindView(R.id.textView_type)
        TextView type;

        @BindView(R.id.textView_status)
        TextView status;

        @BindView(R.id.textView_description)
        TextView description;

        @BindView(R.id.swipe_layout)
        SwipeLayout swipeLayout;

        @BindView(R.id.button_state)
        Button stateButton;

        @BindView(R.id.button_decommission)
        Button decommissionButton;

        private BeaconRegisteredPresenter.BeaconRegisteredItemPresenter itemPresenter;

        public RegisteredBeaconViewHolder(View itemView, boolean bind) {
            super(itemView);
            if (bind) {
                ButterKnife.bind(this, itemView);
                swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);
                swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            }
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            itemPresenter.onBind(position);
        }

        @Override
        public void setItemPresenter(BeaconRegisteredPresenter.BeaconRegisteredItemPresenter beaconRegisteredItemPresenter) {
            itemPresenter = beaconRegisteredItemPresenter;
        }

        @Override
        public void updateItem(RegisteredBeaconModel model) {
            id.setText(model.hexId);
            type.setText(model.type);
            description.setText(model.description);
            status.setText(model.status);
            rowLayout.setOnClickListener(view -> itemPresenter.onClick(model.name, model.status));
            decommissionButton.setOnClickListener(view -> itemPresenter.onDecommissionButtonClicked(model));

            if (BeaconStatus.ACTIVE.name().equals(model.status)) {
                stateButton.setText(R.string.button_deactivate);
                stateButton.setOnClickListener(view -> itemPresenter.onDeactivateButtonClicked(model));
            } else if (BeaconStatus.INACTIVE.name().equals(model.status)) {
                stateButton.setText(R.string.button_activate);
                stateButton.setOnClickListener(view -> itemPresenter.onActivateButtonClicked(model));
            } else {
                swipeLayout.clearDragEdge();
            }
        }
    }
}
