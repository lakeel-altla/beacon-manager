package com.altla.vision.beacon.manager.data.entity;

public final class PreferencesEntity {

    public final String token;

    public final String projectId;

    public PreferencesEntity(String token, String projectId) {
        this.token = token;
        this.projectId = projectId;
    }
}
