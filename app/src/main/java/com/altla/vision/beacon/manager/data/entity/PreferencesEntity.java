package com.altla.vision.beacon.manager.data.entity;

public final class PreferencesEntity {

    public final String mToken;

    public final String mProjectId;

    public PreferencesEntity(String token, String projectId) {
        mToken = token;
        mProjectId = projectId;
    }
}
