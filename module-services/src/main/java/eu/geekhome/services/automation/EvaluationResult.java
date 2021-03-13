package eu.geekhome.services.automation;

import eu.geekhome.services.localization.Resource;

import java.util.ArrayList;

public class EvaluationResult {

    private Object _value;
    private Resource _interfaceValue;
    private ArrayList<KeyValue> _descriptions;
    private ControlMode _controlMode;
    private boolean _isSignaled;
    private String _colorValue;

    public Object getValue() {
        return _value;
    }

    public void setValue(Object value) {
        _value = value;
    }

    public boolean isSignaled() {
        return _isSignaled;
    }

    public void setSignaled(boolean value) {
        _isSignaled = value;
    }

    public Resource getInterfaceValue() {
        return _interfaceValue;
    }

    public String getColorValue() {
        return _colorValue;
    }

    public void setColorValue(String colorValue) {
        _colorValue = colorValue;
    }

    public void setInterfaceValue(Resource value) {
        _interfaceValue = value;
    }

    public ArrayList<KeyValue> getDescriptions() {
        return _descriptions;
    }

    public void setControlMode(ControlMode controlMode) {
        _controlMode = controlMode;
    }

    public ControlMode getControlMode() {
        return _controlMode;
    }

    public void setDescriptions(ArrayList<KeyValue> value) {
        _descriptions = value;
    }

    public EvaluationResult(Object value, Resource interfaceValue, boolean isSignaled,
                            ArrayList<KeyValue> descriptions, ControlMode controlMode) {
        setDescriptions(descriptions);
        setValue(value);
        setInterfaceValue(interfaceValue);
        setControlMode(controlMode);
        setSignaled(isSignaled);
    }

    public EvaluationResult(Object value, Resource interfaceValue, boolean isSignaled,
                            ArrayList<KeyValue> descriptions) {
        this(value, interfaceValue, isSignaled, descriptions, ControlMode.Auto);
    }

    public EvaluationResult(Object value, Resource interfaceValue, boolean isSignaled) {
        this(value, interfaceValue, isSignaled, new ArrayList<>());
    }
}
