package com.geekhome.synchronizationmodule.business;

import java.util.Hashtable;

public class AliveSignalNotification extends NotificationBase {
    public static final String KEY_IS_OPERATIONAL = "isOperational";
    public static final String KEY_CONFIGURATION_VERSION = "configurationVersion";

    public AliveSignalNotification(long timestamp, boolean isOperational, int configurationVersion) {
        super(SynchronizationNotificationType.AliveSignal, timestamp);
        setOperational(isOperational);
        setConfigurationVersion(configurationVersion);
    }

    public AliveSignalNotification(Hashtable<String, String> hashtable) {
        super(hashtable);
    }

    public String getConfigurationVersion() {
        return getProperties().get(KEY_CONFIGURATION_VERSION);
    }

    public void setConfigurationVersion(int value) {
        getProperties().put(KEY_CONFIGURATION_VERSION, String.valueOf(value));
    }

    public boolean isOperational() {
        return Boolean.valueOf(getProperties().get(KEY_IS_OPERATIONAL));
    }

    public void setOperational(boolean value) {
        getProperties().put(KEY_IS_OPERATIONAL, String.valueOf(value));
    }
}