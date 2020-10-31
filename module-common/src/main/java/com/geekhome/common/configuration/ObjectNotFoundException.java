package com.geekhome.common.configuration;

public class ObjectNotFoundException extends Exception {
    private String _uniqueId;

    public ObjectNotFoundException(String uniqueId) {
        this._uniqueId = uniqueId;
    }

    @Override
    public String getMessage() {
        return "Cannot find object of unique id: " + _uniqueId;
    }
}
