package com.geekhome.synchronizationmodule;

import com.google.gson.annotations.SerializedName;

public class SynchronizedState extends SynchronizedNamedObject {
    @SerializedName("type")
    private int _type;

    @SerializedName("isCodeRequired")
    private boolean _isCodeRequired;

    public SynchronizedState() {
    }

    public int getType() {
        return _type;
    }

    public void setType(int type) {
        _type = type;
    }

    public boolean isCodeRequired() {
        return _isCodeRequired;
    }

    public void setCodeRequired(boolean isCodeRequired) {
        _isCodeRequired = isCodeRequired;
    }
}