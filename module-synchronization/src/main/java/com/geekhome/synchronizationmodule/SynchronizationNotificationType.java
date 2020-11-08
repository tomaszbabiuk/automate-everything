package com.geekhome.synchronizationmodule;

public enum SynchronizationNotificationType {
    Alerts("alerts"),
    AliveSignal("systeminfo");

    private String _command;

    SynchronizationNotificationType(String command) {
        _command = command;
    }

    @Override
    public String toString() {
        return _command;
    }

    public static SynchronizationNotificationType fromString(String type) {
        for(SynchronizationNotificationType value : values()) {
            if (value.toString().equals(type)) {
                return value;
            }
        }

        return null;
    }
}
