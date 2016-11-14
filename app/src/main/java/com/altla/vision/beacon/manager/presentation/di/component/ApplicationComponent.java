package com.altla.vision.beacon.manager.presentation.di.component;

import com.altla.vision.beacon.manager.presentation.di.module.ActivityModule;
import com.altla.vision.beacon.manager.presentation.di.module.ApplicationModule;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    UserComponent userComponent(ActivityModule module);

    Context context();
}

