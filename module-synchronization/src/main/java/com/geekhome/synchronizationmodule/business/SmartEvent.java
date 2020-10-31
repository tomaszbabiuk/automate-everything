package com.geekhome.synchronizationmodule.business;

public enum SmartEvent {
    NfcTagScanned("nfcscanned"),
    LocationReported("locationreported"),
    GeofenceEnter("geofenceenter"),
    GeofenceExit("geofenceexit");

    private final String _name;

    SmartEvent(String name) {
        _name = name;
    }

    @Override
    public String toString() {
        return _name;
    }

    public static SmartEvent fromString(String command) {
        for(SmartEvent value : values()) {
            if (value.toString().equals(command)) {
                return value;
            }
        }

        return null;
    }
}
