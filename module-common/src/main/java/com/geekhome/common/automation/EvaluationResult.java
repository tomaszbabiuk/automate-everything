package com.geekhome.common.automation;

import com.geekhome.common.KeyValue;
import com.geekhome.common.configuration.Persistable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EvaluationResult {
    private Object _value;
    private String _interfaceValue;
    private List<KeyValue> _descriptions;
    private ControlMode _controlMode;
    private boolean _isSignaled;
    private boolean _isConnected;
    private boolean _isAlternating;
    private String _colorValue;

    @Persistable(name="Value")
    public Object getValue() {
        return _value;
    }

    public void setValue(Object value) {
        _value = value;
    }

    @Persistable(name="IsSignaled")
    public boolean isSignaled() {
        return _isSignaled;
    }

    public void setSignaled(boolean value) {
        _isSignaled = value;
    }

    @Persistable(name="IsConnected")
    public boolean isConnected() {
        return _isConnected;
    }

    public void setConnected(boolean value) {
        _isConnected = value;
    }

    @Persistable(name="IsAlternating")
    public boolean isAlternating() {
        return _isAlternating;
    }

    public void setIsAlternating(boolean value) {
        _isAlternating = value;
    }

    @Persistable(name="InterfaceValue")
    public String getInterfaceValue() {
        return _interfaceValue;
    }

    @Persistable(name="ColorValue")
    public String getColorValue() {
        return _colorValue;
    }

    public void setColorValue(String colorValue) {
        _colorValue = colorValue;
    }

    public void setInterfaceValue(String value) {
        _interfaceValue = value;
    }

    @Persistable(name="Descriptions")
    public List<KeyValue> getDescriptions() {
        return _descriptions;
    }

    public void setControlMode(ControlMode controlMode) {
        _controlMode = controlMode;
    }

    @Persistable(name="ControlMode")
    public ControlMode getControlMode() {
        return _controlMode;
    }

    public void setDescriptions(List<KeyValue> value) {
        _descriptions = value;
    }

    public EvaluationResult(Object value, String interfaceValue, boolean isSignaled, boolean isConnected,
                            List<KeyValue> descriptions, ControlMode controlMode, boolean isAlternating) {
        setDescriptions(descriptions);
        setValue(value);
        setInterfaceValue(interfaceValue);
        setControlMode(controlMode);
        setSignaled(isSignaled);
        setIsAlternating(isAlternating);
        setConnected(isConnected);
    }

    public EvaluationResult(Object value, String interfaceValue, boolean isSignaled, boolean isConnected,
                            List<KeyValue> descriptions) {
        this(value, interfaceValue, isSignaled, isConnected, descriptions, ControlMode.Auto, false);
    }

    public EvaluationResult(Object value, String interfaceValue, boolean isSignaled, boolean isConnected) {
        this(value, interfaceValue, isSignaled, isConnected, new ArrayList<>());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvaluationResult that = (EvaluationResult) o;
        return _isSignaled == that._isSignaled &&
                _isAlternating == that._isAlternating &&
                Objects.equals(_value, that._value) &&
                Objects.equals(_interfaceValue, that._interfaceValue) &&
                Objects.equals(_descriptions, that._descriptions) &&
                _controlMode == that._controlMode &&
                Objects.equals(_colorValue, that._colorValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_value, _interfaceValue, _descriptions != null ? _descriptions.toString() : 0, _controlMode, _isSignaled, _isAlternating, _colorValue);
    }
}
