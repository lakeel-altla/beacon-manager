package com.altla.vision.beacon.manager.presentation.view.adapter;

import android.support.annotation.IntRange;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.presentation.constants.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.presenter.BeaconListPresenter;
import com.altla.vision.beacon.manager.presentation.presenter.model.BeaconListModel;
import com.altla.vision.beacon.manager.presentation.view.BeaconListItemView;
import com.marshalchen.ultimaterecyclerview.SwipeableUltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class BeaconListAdapter extends SwipeableUltimateViewAdapter<BeaconListModel> {

    private BeaconListPresenter beaconListPresenter;

    public BeaconListAdapter(BeaconListPresenter presenter) {
        super(new ArrayList<>());
        beaconListPresenter = presenter;
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.beacon_registered_item;
    }

    @Override
    protected UltimateRecyclerviewViewHolder newViewHolder(View view) {
        BeaconListViewHolder beaconListViewHolder = new BeaconListViewHolder(view, true);
        beaconListPresenter.onCreateItemView(beaconListViewHolder);
        return beaconListViewHolder;
    }

    @Override
    protected void withBindHolder(UltimateRecyclerviewViewHolder bindHolder, BeaconListModel model, int position) {
        BeaconListViewHolder holder = (BeaconListViewHolder) bindHolder;
        holder.onBind(position);
    }

    final class BeaconListViewHolder extends UltimateRecyclerviewViewHolder implements BeaconListItemView {

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

        private BeaconListPresenter.BeaconListIemPresenter itemPresenter;

        public BeaconListViewHolder(View itemView, boolean bind) {
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
        public void setItemPresenter(BeaconListPresenter.BeaconListIemPresenter beaconListIemPresenter) {
            itemPresenter = beaconListIemPresenter;
        }

        @Override
        public void updateItem(BeaconListModel model) {
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
