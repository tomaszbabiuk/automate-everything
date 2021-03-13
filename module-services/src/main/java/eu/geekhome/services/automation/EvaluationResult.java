package eu.geekhome.services.automation;

import eu.geekhome.services.localization.Resource;

import java.util.HashMap;
import java.util.Map;

public class EvaluationResult {

    private Object _value;
    private Resource _interfaceValue;
    private Map<String, Resource> _descriptions;
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

    public Map<String, Resource> getDescriptions() {
        return _descriptions;
    }

    public void setControlMode(ControlMode controlMode) {
        _controlMode = controlMode;
    }

    public ControlMode getControlMode() {
        return _controlMode;
    }

    public void setDescriptions(Map<String, Resource> value) {
        _descriptions = value;
    }

    public EvaluationResult(Object value, Resource interfaceValue, boolean isSignaled,
                            Map<String, Resource> descriptions, ControlMode controlMode) {
        setDescriptions(descriptions);
        setValue(value);
        setInterfaceValue(interfaceValue);
        setControlMode(controlMode);
        setSignaled(isSignaled);
    }

    public EvaluationResult(Object value, Resource interfaceValue, boolean isSignaled,
                            Map<String, Resource> descriptions) {
        this(value, interfaceValue, isSignaled, descriptions, ControlMode.Auto);
    }

    public EvaluationResult(Object value, Resource interfaceValue, boolean isSignaled) {
        this(value, interfaceValue, isSignaled, new HashMap<>());
    }
}
