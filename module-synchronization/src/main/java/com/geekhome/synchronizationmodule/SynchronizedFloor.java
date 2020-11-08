package com.geekhome.synchronizationmodule;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class SynchronizedFloor extends SynchronizedNamedObject {
    @SerializedName("rooms")
    private Map<String, SynchronizedRoom> _rooms;

    @SerializedName("iconName")
    private String _iconName;

    public SynchronizedFloor() {
        setRooms(new HashMap<>());
    }

    public Map<String,SynchronizedRoom> getRooms() {
        return _rooms;
    }

    public void setRooms(Map<String, SynchronizedRoom> rooms) {
        _rooms = rooms;
    }

    public String getIconName() {
        return _iconName;
    }

    public void setIconName(String iconName) {
        _iconName = iconName;
    }
}