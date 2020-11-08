package com.geekhome.synchronizationmodule;

import com.google.gson.annotations.SerializedName;

public enum SynchronizationCommandType {
    @SerializedName("refreshfloors")
    RefreshFloors("refreshfloors", SynchronizationContentType.Floors),

    @SerializedName("refreshdevices")
    RefreshDevices("refreshdevices", SynchronizationContentType.Devices),

    @SerializedName("refreshevaluations")
    RefreshEvaluations("refreshevaluations", SynchronizationContentType.Evaluations),

    @SerializedName("refreshgeofences")
    RefreshGeofences("refreshgeofences", SynchronizationContentType.Geofences),

    @SerializedName("controldevice")
    ControlDevice("controldevice", SynchronizationContentType.Evaluations),

    @SerializedName("registerdevice")
    RegisterDevice("registerdevice", SynchronizationContentType.RegistrationConfirmed),

    @SerializedName("executecommand")
    ExecuteCommand("executecommand", SynchronizationContentType.CommandResult),

    @SerializedName("reportsmartevent")
    ReportSmartEvent("reportsmartevent", SynchronizationContentType.SmartEventResult),

    @SerializedName("ping")
    Ping("ping", SynchronizationContentType.Ping),

    @SerializedName("systeminfo")
    GetSystemInfo("systeminfo", SynchronizationContentType.SystemInfo);

    private String _command;
    private SynchronizationContentType _contentType;

    SynchronizationCommandType(String command, SynchronizationContentType contentType) {
        _command = command;
        _contentType = contentType;
    }

    @Override
    public String toString() {
        return _command;
    }

    public static SynchronizationCommandType fromString(String command) {
        for(SynchronizationCommandType value : values()) {
            if (value.toString().equals(command)) {
                return value;
            }
        }

        return null;
    }

    public SynchronizationContentType getContentType() {
        return _contentType;
    }
}
