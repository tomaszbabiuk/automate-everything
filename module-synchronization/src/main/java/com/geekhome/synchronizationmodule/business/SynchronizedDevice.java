package com.geekhome.synchronizationmodule.business;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class SynchronizedDevice extends SynchronizedNamedObject {
    @SerializedName("category")
    private int _category;

    @SerializedName("controlType")
    private int _controlType;

    @SerializedName("roomsIds")
    private String _roomsIds;

    @SerializedName("hasNonReadonlyStates")
    private Boolean _hasNonReadonlyStated;

    @SerializedName("states")
    private Map<String, SynchronizedState> _states;

    @SerializedName("iconName")
    private String _iconName;

    @SerializedName("modes")
    private Map<String, SynchronizedNamedObject> _modes;

    @SerializedName("initialValue")
    private double _initialValue;

    @SerializedName("minValue")
    private double _minValue;

    @SerializedName("maxValue")
    private double _maxValue;

    @SerializedName("unit")
    private String _unit;

    @SuppressWarnings("UnusedDeclaration")
    public SynchronizedDevice() {
    }

    public int getCategory() {
        return _category;
    }

    private transient SynchronizedEvaluationResult _evaluationResult;

    public void setCategory(int category) {
        _category = category;
    }

    public int getControlType() {
        return _controlType;
    }

    public void setControlType(int controlType) {
        _controlType = controlType;
    }

    public String getRoomsIds() {
        return _roomsIds;
    }

    public void setRoomId(String roomId) {
        _roomsIds = roomId;
    }

    public Boolean getHasNonReadonlyStated() {
        return _hasNonReadonlyStated;
    }

    public void setHasNonReadonlyStated(Boolean hasNonReadonlyStated) {
        _hasNonReadonlyStated = hasNonReadonlyStated;
    }

    public Map<String, SynchronizedState> getStates() {
        return _states;
    }

    public void setStates(Map<String, SynchronizedState> states) {
        _states = states;
    }

    public String getIconName() {
        return _iconName;
    }

    public void setIconName(String iconName) {
        _iconName = iconName;
    }

    public SynchronizedEvaluationResult getEvaluationResult() {
        return _evaluationResult;
    }

    public void setEvaluationResult(SynchronizedEvaluationResult evaluationResult) {
        _evaluationResult = evaluationResult;
    }

    public Map<String, SynchronizedNamedObject> getModes() {
        return _modes;
    }

    public void setModes(Map<String, SynchronizedNamedObject> modes) {
        _modes = modes;
    }

    public double getInitialValue() {
        return _initialValue;
    }

    public void setInitialValue(double initialValue) {
        _initialValue = initialValue;
    }

    public double getMinValue() {
        return _minValue;
    }

    public void setMinValue(double minValue) {
        _minValue = minValue;
    }

    public double getMaxValue() {
        return _maxValue;
    }

    public void setMaxValue(double maxValue) {
        _maxValue = maxValue;
    }

    public String getUnit() {
        return _unit;
    }

    public void setUnit(String unit) {
        _unit = unit;
    }
}