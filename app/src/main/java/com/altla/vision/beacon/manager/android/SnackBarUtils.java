package com.altla.vision.beacon.manager.android;

import android.support.design.widget.Snackbar;
import android.view.View;

public final class SnackBarUtils {

    private SnackBarUtils() {
    }

    public static void showShort(View view, int resId) {
        show(view, resId, Snackbar.LENGTH_SHORT);
    }

    public static void showLong(View view, int resId) {
        show(view, resId, Snackbar.LENGTH_LONG);
    }

    private static void show(View view, int resId, int length) {
        String message = view.getResources().getString(resId);
        Snackbar.make(view, message, length)
                .show();
    }
}