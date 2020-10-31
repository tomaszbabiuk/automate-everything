package com.geekhome.common.configuration;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.NamedObject;
import com.geekhome.common.configuration.Persistable;

public class Geofence extends NamedObject {
    private double _latitude;
    private double _longitude;
    private double _radius;
    private String _iconName;

    @Persistable(name = "Latitude")
    public double getLatitude() {
        return _latitude;
    }

    public void setLatitude(double value) {
        _latitude = value;
    }

    @Persistable(name = "Longitude")
    public double getLongitude() {
        return _longitude;
    }

    public void setLongitude(double value) {
        _longitude = value;
    }

    @Persistable(name = "Radius")
    public double getRadius() {
        return _radius;
    }

    public void setRadius(double radius) {
        _radius = radius;
    }

    @Persistable(name="IconName")
    public String getIconName() {
        return _iconName;
    }

    public void setIconName(String value) {
        _iconName = value;
    }

    public Geofence(DescriptiveName name, String iconName, double longitude, double latitude, double radius) {
        super(name);
        setIconName(iconName);
        setLatitude(latitude);
        setLongitude(longitude);
        setRadius(radius);
    }
}
