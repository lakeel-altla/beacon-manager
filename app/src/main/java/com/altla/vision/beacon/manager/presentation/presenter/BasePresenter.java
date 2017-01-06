package com.altla.vision.beacon.manager.presentation.presenter;

import rx.subscriptions.CompositeSubscription;

public class BasePresenter<V> {

    private V v;

    CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public void onCreateView(V v) {
        this.v = v;
    }

    public void onResume() {
    }

    public void onStop() {
        mCompositeSubscription.unsubscribe();
    }

    protected V getView() {
        return v;
    }
}
