package com.geekhome.synchronizationmodule.business;

import com.google.gson.annotations.SerializedName;

public class SmartEventResult {
    @SerializedName("success")
    private boolean _success;

    public SmartEventResult(boolean success) {
        _success = success;
    }

    public boolean isSuccess() {
        return _success;
    }
}
