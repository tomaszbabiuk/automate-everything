package com.geekhome.synchronizationmodule;

import com.google.gson.annotations.SerializedName;

public class SynchronizedRoom extends SynchronizedNamedObject {
    @SerializedName("iconName")
    private String _iconName;

    @SerializedName("devicesCount")
    private int _devicesCount;

    public SynchronizedRoom() {
        _devicesCount = -1;
    }

    public String getIconName() {
        return _iconName;
    }

    public void setIconName(String iconName) {
        _iconName = iconName;
    }

    public int getDevicesCount() {
        return _devicesCount;
    }

    public void setDevicesCount(int devicesCount) {
        _devicesCount = devicesCount;
    }
}