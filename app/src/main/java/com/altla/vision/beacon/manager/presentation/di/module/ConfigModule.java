package com.altla.vision.beacon.manager.presentation.di.module;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ConfigModule {

    @Provides
    @Named("bearer")
    public String provideBearer() {
        return "Bearer ";
    }

    @Provides
    @Named("proximityBaseUri")
    public String provideProximityBaseUri() {
        return "https://proximitybeacon.googleapis.com/v1beta1/";
    }
}
