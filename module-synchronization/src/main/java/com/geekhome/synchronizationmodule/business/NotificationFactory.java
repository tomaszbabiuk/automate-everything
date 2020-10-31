package com.geekhome.synchronizationmodule.business;

import java.util.Hashtable;

public class NotificationFactory {
    public static NotificationBase create(Hashtable<String, String> hashtable) {
        SynchronizationNotificationType type = NotificationBase.ckeckType(hashtable);
        switch (type) {
            case Alerts:
                return new AlertNotification(hashtable);
            case AliveSignal:
                return new AliveSignalNotification(hashtable);
            default:
                throw new IllegalArgumentException("Cannot create notification. Wrong set of keys!");
        }
    }
}
