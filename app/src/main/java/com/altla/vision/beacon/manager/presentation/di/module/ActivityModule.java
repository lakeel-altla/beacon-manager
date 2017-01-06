package com.altla.vision.beacon.manager.presentation.di.module;

import android.app.Activity;
import android.support.annotation.NonNull;

import dagger.Module;

@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(@NonNull Activity activity) {
        this.activity = activity;
    }
}
