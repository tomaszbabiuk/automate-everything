package com.geekhome.synchronizationmodule;

import com.google.gson.annotations.SerializedName;

public class SynchronizationRequest {
    @SerializedName("code")
    private final String _code;

    @SerializedName("type")
    private final SynchronizationCommandType _type;

    @SerializedName("parameter")
    private final String _parameter;

    @SerializedName("value")
    private final String _value;

    public SynchronizationRequest(SynchronizationCommandType type, String parameter, String value, String code) {
        _type = type;
        _parameter = parameter;
        _value = value;
        _code = code;
    }

    public SynchronizationRequest(SynchronizationCommandType type) {
        this(type, null, null, null);
    }

    public SynchronizationCommandType getType() {
        return _type;
    }

    public String getParameter() {
        return _parameter;
    }

    public String getCode() {
        return _code;
    }

    public String getValue() {
        return _value;
    }
}
