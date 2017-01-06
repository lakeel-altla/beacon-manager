package com.altla.vision.beacon.manager.data.entity;

import java.util.Map;

public final class BeaconEntity {

    public String beaconName;

    public AdvertisedId advertisedId;

    public String status;

    public String placeId;

    public LatLng latLng;

    public IndoorLevel indoorLevel;

    public String expectedStability;

    public String description;

    public Map<String, String> properties;

    public static class AdvertisedId {

        public String type;

        public String id;
    }

    public static class LatLng {

        public double latitude;

        public double longitude;
    }

    public static class IndoorLevel {

        public String name;
    }
}
