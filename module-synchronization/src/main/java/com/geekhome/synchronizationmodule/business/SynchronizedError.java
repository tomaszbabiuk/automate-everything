package com.geekhome.synchronizationmodule.business;

import com.google.gson.annotations.SerializedName;

public class SynchronizedError {
    @SerializedName("simplifiedClass")
    private String _simplifiedClass;

    @SerializedName("message")
    private String _message;

    public SynchronizedError() {
    }

    public SynchronizedError(Exception ex) {
        _message = ex.getMessage();
        _simplifiedClass = ex.getClass().getSimpleName();
    }

    public String getSimplifiedClass() {
        return _simplifiedClass;
    }

    public void setSimplifiedClass(String simplifiedClass) {
        _simplifiedClass = simplifiedClass;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }
}