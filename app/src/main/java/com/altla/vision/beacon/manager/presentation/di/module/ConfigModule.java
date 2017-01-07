package com.altla.vision.beacon.manager.presentation.di.module;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ConfigModule {

    @Provides
    @Named("proximityBeaconBaseUri")
    String provideProximityBeaconBaseUri() {
        return "https://proximitybeacon.googleapis.com/v1beta1/";
    }

    @Provides
    @Named("connectTimeout")
    long provideConnectTimeout() {
        return 10 * 1000;
    }

    @Provides
    @Named("readTimeout")
    long provideReadTimeout() {
        return 10 * 1000;
    }
}
