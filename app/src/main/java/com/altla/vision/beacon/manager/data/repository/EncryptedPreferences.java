package com.altla.vision.beacon.manager.data.repository;

import com.orhanobut.hawk.Hawk;

import android.content.Context;

public final class EncryptedPreferences {

    private static final String TOKEN_KEY = "token";

    private static final String PROJECT_ID_KEY = "mProjectId";

    public EncryptedPreferences(Context context) {
        Hawk.init(context).build();
    }

    public void saveToken(String token) {
        setString(TOKEN_KEY, token);
    }

    public String getToken() {
        return getString(TOKEN_KEY);
    }

    public void saveProjectId(String projectId) {
        setString(PROJECT_ID_KEY, projectId);
    }

    public String getProjectId() {
        return getString(PROJECT_ID_KEY);
    }

    private void setString(String key, String value) {
        // AES で暗号化。
        Hawk.put(key, value);
    }

    private String getString(String key) {
        return Hawk.get(key);
    }
}
