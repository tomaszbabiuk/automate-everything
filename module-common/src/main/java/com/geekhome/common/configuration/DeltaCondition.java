package com.geekhome.common.configuration;

import com.geekhome.common.EqualityOperator;

public class DeltaCondition extends ConditionBase {
    private String _firstDeviceId;
    private String _secondDeviceId;
    private int _delta;
    private double _hysteresis;
    private EqualityOperator _operator;

    @SuppressWarnings("WeakerAccess")
    @Persistable(name = "FirstDeviceId")
    public String getFirstDeviceId() {
        return _firstDeviceId;
    }

    @SuppressWarnings("WeakerAccess")
    public void setFirstDeviceId(String value) {
        _firstDeviceId = value;
    }

    @SuppressWarnings("WeakerAccess")
    @Persistable(name = "SecondDeviceId")
    public String getSecondDeviceId() {
        return _secondDeviceId;
    }

    @SuppressWarnings("WeakerAccess")
    public void setSecondDeviceId(String value) {
        _secondDeviceId = value;
    }

    @SuppressWarnings("WeakerAccess")
    @Persistable(name = "Delta")
    public int getDelta() {
        return _delta;
    }

    @SuppressWarnings("WeakerAccess")
    public void setDelta(int value) {
        _delta = value;
    }

    @SuppressWarnings("WeakerAccess")
    @Persistable(name = "Hysteresis")
    public double getHysteresis() {
        return _hysteresis;
    }

    @SuppressWarnings("WeakerAccess")
    public void setHysteresis(double value) {
        _hysteresis = value;
    }

    @SuppressWarnings("WeakerAccess")
    @Persistable(name = "Operator")
    public EqualityOperator getOperator() {
        return _operator;
    }

    @SuppressWarnings("WeakerAccess")
    public void setOperator(EqualityOperator value) {
        _operator = value;
    }

    @Override
    public String[] getDevicesIds() {
        String[] result = new String[2];
        result[0] = getFirstDeviceId();
        result[1] = getSecondDeviceId();
        return result;
    }

    public DeltaCondition(DescriptiveName name, EqualityOperator equalityOperator, int delta,
                          double hysteresis, String firstDeviceId, String secondDeviceId) {
        super(name);
        setDelta(delta);
        setHysteresis(hysteresis);
        setFirstDeviceId(firstDeviceId);
        setSecondDeviceId(secondDeviceId);
        setOperator(equalityOperator);
    }
}