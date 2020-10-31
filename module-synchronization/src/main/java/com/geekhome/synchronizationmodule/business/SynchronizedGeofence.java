package com.geekhome.synchronizationmodule.business;

import com.google.gson.annotations.SerializedName;

public class SynchronizedGeofence extends SynchronizedNamedObject {
    @SerializedName("longitude")
    private double _longitude;

    @SerializedName("latitude")
    private double _latitude;

    @SerializedName("radius")
    private double _radius;

    @SerializedName("iconName")
    private String _iconName;

    public double getLongitude() {
        return _longitude;
    }

    public void setLongitude(double longitude) {
        _longitude = longitude;
    }

    public double getLatitude() {
        return _latitude;
    }

    public void setLatitude(double latitude) {
        _latitude = latitude;
    }

    public double getRadius() {
        return _radius;
    }

    public void setRadius(double radius) {
        _radius = radius;
    }

    public String getIconName() {
        return _iconName;
    }

    public void setIconName(String iconName) {
        _iconName = iconName;
    }
}