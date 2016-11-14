package com.altla.vision.beacon.manager.presentation.application;

import com.altla.vision.beacon.manager.presentation.di.component.ApplicationComponent;
import com.altla.vision.beacon.manager.presentation.di.component.DaggerApplicationComponent;
import com.altla.vision.beacon.manager.presentation.di.module.ApplicationModule;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;

public class App extends Application {

    private ApplicationComponent mApplicationComponent;

    private static final String I_BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private static final String EDDYSTONE_EID_LAYOUT = "s:0-1=feaa,m:2-2=30,p:3-3:-41,i:4-11";

    @Override
    public void onCreate() {
        super.onCreate();

        System.setProperty("http.keepAlive", "false");

        // Create singleton instance.
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

        // Beacon frame settings.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(I_BEACON_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(EDDYSTONE_EID_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static ApplicationComponent getApplicationComponent(@NonNull Activity activity) {
        return ((App) activity.getApplication()).mApplicationComponent;
    }
}
