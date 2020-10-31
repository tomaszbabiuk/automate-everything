package com.geekhome.common.configuration;

import com.geekhome.common.configuration.ConditionBase;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.EqualityOperator;
import com.geekhome.common.configuration.Persistable;

public class ValueCondition extends ConditionBase {
    private String _deviceId;
    private EqualityOperator _operator;
    private double _value;
    private double _hysteresis;

    @Persistable(name="DeviceId")
    public String getDeviceId() {
        return _deviceId;
    }

    public void setDeviceId(String value) {
        _deviceId = value;
    }

    @Persistable(name="Operator")
    public EqualityOperator getOperator() {
        return _operator;
    }

    public void setOperator(EqualityOperator value) {
        _operator = value;
    }

    @Persistable(name="Value")
    public double getValue() {
        return _value;
    }

    public void setValue(double value) {
        _value = value;
    }

    @Persistable(name = "Hysteresis")
    public double getHysteresis() {
        return _hysteresis;
    }

    public void setHysteresis(double value) {
        _hysteresis = value;
    }

    @Override
    public String[] getDevicesIds() {
        String[] result = new String[1];
        result[0] = getDeviceId();
        return result;
    }

    public ValueCondition(DescriptiveName name, String deviceId, EqualityOperator operator, double value, double hysteresis) {
        super(name);
        setDeviceId(deviceId);
        setOperator(operator);
        setValue(value);
        setHysteresis(hysteresis);
    }
}
