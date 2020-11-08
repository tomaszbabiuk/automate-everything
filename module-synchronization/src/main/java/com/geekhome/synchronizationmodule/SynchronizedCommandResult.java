package com.geekhome.synchronizationmodule;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class SynchronizedCommandResult {
    @SerializedName("output")
    private String _output;

    @SerializedName("evaluations")
    private Map<String, SynchronizedEvaluationResult> _evaluations;

    @SerializedName("codeRequired")
    private boolean _codeRequired;

    @SerializedName("controlledDevice")
    private SynchronizedDevice _controlledDevice;

    public SynchronizedCommandResult() {
    }

    public String getOutput() {
        return _output;
    }

    public Map<String, SynchronizedEvaluationResult> getEvaluations() {
        return _evaluations;
    }

    public void setOutput(String output) {
        _output = output;
    }

    public void setEvaluations(Map<String, SynchronizedEvaluationResult> evaluations) {
        _evaluations = evaluations;
    }

    public boolean isCodeRequired() {
        return _codeRequired;
    }

    public void setCodeRequired(boolean codeRequired) {
        _codeRequired = codeRequired;
    }

    public SynchronizedDevice getControlledDevice() {
        return _controlledDevice;
    }

    public void setControlledDevice(SynchronizedDevice device) {
        _controlledDevice = device;
    }
}
