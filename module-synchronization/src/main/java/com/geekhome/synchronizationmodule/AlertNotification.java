package com.geekhome.synchronizationmodule;

import java.util.Hashtable;

public class AlertNotification extends NotificationBase {
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_UNIQUE_ID = "uniqueId";
    public static final String KEY_DEVICES_IDS = "devicesIds";
    public static final String KEY_DEVICES_VALUES = "devicesValues";
    public static final String KEY_DEVICES_NAMES = "devicesNames";
    public static final String KEY_IS_ON = "isOn";

    public AlertNotification(long timestamp, String name, String description, String uniqueId, String devicesIds, 
                             String devicesValues, String devicesNames, boolean isOn) {
        super(SynchronizationNotificationType.Alerts, timestamp);
        setName(name);
        setDescription(description);
        setUniqueId(uniqueId);
        setDevicesIds(devicesIds);
        setDevicesValues(devicesValues);
        setDevicesNames(devicesNames);
        setIsOn(isOn);
    }

    public AlertNotification(Hashtable<String, String> hashtable) {
        super(hashtable);
    }

    public String getName() {
        return getProperties().get(KEY_NAME);
    }

    public void setName(String value) {
        getProperties().put(KEY_NAME, value);
    }

    public String getDescription() {
        return getProperties().get(KEY_DESCRIPTION);
    }

    public void setDescription(String value) {
        getProperties().put(KEY_DESCRIPTION, value);
    }

    public String getUniqueId() {
        return getProperties().get(KEY_UNIQUE_ID);
    }

    public void setUniqueId(String value) {
        getProperties().put(KEY_UNIQUE_ID, value);
    }

    public String getDevicesIds() {
        return getProperties().get(KEY_DEVICES_IDS);
    }

    public void setDevicesIds(String value) {
        getProperties().put(KEY_DEVICES_IDS, value);
    }

    public String getDevicesValues() {
        return getProperties().get(KEY_DEVICES_VALUES);
    }

    public void setDevicesValues(String value) {
        getProperties().put(KEY_DEVICES_VALUES, value);
    }

    public String getDevicesNames() {
        return getProperties().get(KEY_DEVICES_NAMES);
    }

    public void setDevicesNames(String value) {
        getProperties().put(KEY_DEVICES_NAMES, value);
    }

    public boolean isOn() {
        return Boolean.valueOf(getProperties().get(KEY_IS_ON));
    }

    public void setIsOn(boolean value) {
        getProperties().put(KEY_IS_ON, String.valueOf(value));
    }
}