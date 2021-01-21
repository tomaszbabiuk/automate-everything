package com.geekhome.synchronizationmodule;

import java.util.Hashtable;

public class NotificationBase {
    public static final String KEY_TYPE = "type";
    public static final String KEY_TIMESTAMP = "timestamp";

    private final Hashtable<String, String> _properties;

    public Hashtable<String, String> getProperties() {
        return _properties;
    }

    public NotificationBase(SynchronizationNotificationType type, long timestamp) {
        _properties = new Hashtable<>();
        setType(type);
        setTimestamp(timestamp);
    }

    public NotificationBase(Hashtable<String, String> hashtable) {
        _properties = hashtable;
    }

    public void setType(SynchronizationNotificationType type) {
        _properties.put(KEY_TYPE, type.toString());
    }

    public SynchronizationNotificationType getType() {
        return SynchronizationNotificationType.fromString(_properties.get(KEY_TYPE));
    }

    public void setTimestamp(long timestamp) {
        _properties.put(KEY_TIMESTAMP, String.valueOf(timestamp));
    }

    public long getTimestamp() {
        return Long.valueOf(_properties.get(KEY_TIMESTAMP));
    }

    public static SynchronizationNotificationType ckeckType(Hashtable<String, String> hashtable) {
        return SynchronizationNotificationType.fromString(hashtable.get(KEY_TYPE));
    }
}
