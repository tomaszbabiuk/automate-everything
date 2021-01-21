package com.geekhome.synchronizationmodule;

import com.google.gson.annotations.SerializedName;

public enum SynchronizationContentType {
    @SerializedName("evaluations")
    Evaluations("evaluations"),

    @SerializedName("command")
    CommandResult("command"),

    @SerializedName("smart")
    SmartEventResult("smart"),

    @SerializedName("devices")
    Devices("devices"),

    @SerializedName("floors")
    Floors("floors"),

    @SerializedName("unknown")
    Unknown("unknown"),

    @SerializedName("error")
    Error("error"),

    @SerializedName("confirmation")
    RegistrationConfirmed("confirmation"),

    @SerializedName("ping")
    Ping("ping"),

    @SerializedName("systeminfo")
    SystemInfo("systeminfo"),

    @SerializedName("location")
    LocationResult("location"),

    @SerializedName("geofences")
    Geofences("geofences");

    private final String _str;

    SynchronizationContentType(String str) {
        _str = str;
    }

    public static SynchronizationContentType fromString(String contentTypeString) {
        for(SynchronizationContentType value : values()) {
            if (value.toString().equals(contentTypeString)) {
                return value;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return _str;
    }
}
