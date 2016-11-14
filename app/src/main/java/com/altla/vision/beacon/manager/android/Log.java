package com.altla.vision.beacon.manager.android;

import android.support.annotation.NonNull;

//
// Proguard 利用時に DEBUG ログを削除する方法。
// http://kokufu.blogspot.jp/2014/08/android-log.html
//
public final class Log {

    private final String mTag;

    private Log(String tag) {
        mTag = tag;
    }

    public static Log getLog(Class<?> clazz) {
        return new Log(clazz.getSimpleName());
    }

    public void d(@NonNull String message) {
        android.util.Log.d(mTag, message);
    }

    public void d(@NonNull String message, @NonNull Throwable throwable) {
        android.util.Log.d(mTag, message, throwable);
    }

    public void i(@NonNull String message) {
        android.util.Log.i(mTag, message);
    }

    public void i(@NonNull String message, @NonNull Throwable throwable) {
        android.util.Log.i(mTag, message, throwable);
    }

    public void w(@NonNull String message) {
        android.util.Log.w(mTag, message);
    }

    public void w(@NonNull String message, @NonNull Throwable throwable) {
        android.util.Log.w(mTag, message, throwable);
    }

    public void e(@NonNull String message) {
        android.util.Log.e(mTag, message);
    }

    public void e(@NonNull String message, @NonNull Throwable throwable) {
        android.util.Log.e(mTag, message, throwable);
    }
}

