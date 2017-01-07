package com.altla.vision.beacon.manager.presentation.presenter;

import android.support.annotation.CallSuper;

import com.altla.vision.beacon.manager.rx.ReusableCompositeSubscription;

public class BasePresenter<V> {

    ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    private V v;

    protected V getView() {
        return v;
    }

    @CallSuper
    public void onCreateView(V v) {
        this.v = v;
    }

    @CallSuper
    public void onStop() {
        subscriptions.unSubscribe();
    }
}
