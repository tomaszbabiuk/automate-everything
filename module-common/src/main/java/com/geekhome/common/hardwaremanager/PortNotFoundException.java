package com.geekhome.common.hardwaremanager;

public class PortNotFoundException extends Exception {

    private String _uniqueId;
    private PortType _type;

    public PortType getType() {
        return _type;
    }

    public String getUniqueId() {
        return _uniqueId;
    }

    public PortNotFoundException(String uniqueId, PortType type) {
        _uniqueId = uniqueId;
        _type = type;
    }

    @Override
    public String getMessage() {
        return "Cannot find port of unique id: " + _uniqueId;
    }
}
