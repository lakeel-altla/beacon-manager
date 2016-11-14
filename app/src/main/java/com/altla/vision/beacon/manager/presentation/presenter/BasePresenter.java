package com.altla.vision.beacon.manager.presentation.presenter;

import rx.subscriptions.CompositeSubscription;

public class BasePresenter<V> {

    protected V mV;

    protected CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public void onCreateView(V v) {
        mV = v;
    }

    public void onResume() {
    }

    public void onStop() {
        mCompositeSubscription.unsubscribe();
    }

    protected V getView() {
        return mV;
    }
}
