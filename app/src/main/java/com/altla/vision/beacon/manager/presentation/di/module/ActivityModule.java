package com.altla.vision.beacon.manager.presentation.di.module;

import android.app.Activity;
import android.support.annotation.NonNull;

import dagger.Module;

@Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(@NonNull Activity activity) {
        mActivity = activity;
    }
}
