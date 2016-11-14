package com.altla.vision.beacon.manager.android;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

public final class PermissionUtils {

    private PermissionUtils() {
    }

    public static int checkFineLocation(@NonNull Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestAccessFineLocationPermissions(Activity activity, int requestCode) {
        activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
    }
}
