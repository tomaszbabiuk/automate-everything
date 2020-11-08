package com.geekhome.synchronizationmodule;

import com.google.gson.annotations.SerializedName;

public class SynchronizedDescription {
    @SerializedName("key")
    private String _key;

    @SerializedName("value")
    private String _value;

    public SynchronizedDescription() {
    }

    public String getKey() {
        return _key;
    }

    public void setKey(String key) {
        _key = key;
    }

    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        _value = value;
    }
}