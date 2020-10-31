package com.geekhome.synchronizationmodule.business;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class SynchronizedSystemInfo {
    @SerializedName("systemVersion")
    private String _systemVersion;

    @SerializedName("configVersion")
    private int _configVersion;

    @SerializedName("operationMode")
    private int _operationMode;

    @SerializedName("alerts")
    private Map<String, SynchronizedAlert> _alerts;

    public SynchronizedSystemInfo() {
    }

    public String getSystemVersion() {
        return _systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        _systemVersion = systemVersion;
    }

    public int getConfigVersion() {
        return _configVersion;
    }

    public void setConfigVersion(int configVersion) {
        _configVersion = configVersion;
    }

    public int getOperationMode() {
        return _operationMode;
    }

    public void setOperationMode(int operationMode) {
        _operationMode = operationMode;
    }

    public Map<String, SynchronizedAlert> getAlerts() {
        return _alerts;
    }

    public void setAlerts(Map<String, SynchronizedAlert> alerts) {
        _alerts = alerts;
    }
}
