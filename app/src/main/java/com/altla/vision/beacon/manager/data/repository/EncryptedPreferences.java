package com.altla.vision.beacon.manager.data.repository;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

public final class EncryptedPreferences {

    private static final String ACCOUNT_NAME_KEY = "accountName";

    private static final String TOKEN_KEY = "token";

    private static final String PROJECT_ID_KEY = "projectId";

    public EncryptedPreferences(Context context) {
        Hawk.init(context).build();
    }

    public void saveAccountName(String accountName) {
        setString(ACCOUNT_NAME_KEY, accountName);
    }

    public String getAccountName() {
        return getString(ACCOUNT_NAME_KEY);
    }

    public void removeAccountName() {
        Hawk.delete(ACCOUNT_NAME_KEY);
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
        Hawk.put(key, value);
    }

    private String getString(String key) {
        return Hawk.get(key);
    }
}
