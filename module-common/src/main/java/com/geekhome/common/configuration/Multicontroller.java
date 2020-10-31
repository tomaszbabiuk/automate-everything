package com.geekhome.common.configuration;

import com.geekhome.common.ControlType;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.DeviceCategory;
import com.geekhome.common.configuration.Device;
import com.geekhome.common.configuration.Persistable;

public abstract class Multicontroller extends Device {
    private String _modesIds;
    private double _minValue;
    private double _maxValue;
    private double _initialValue;

    @Persistable(name="ModesIds")
    public String getModesIds() {
        return _modesIds;
    }

    public void setModesIds(String value) {
        _modesIds = value;
    }

    @Persistable(name="MinValue")
    public double getMinValue() {
        return _minValue;
    }

    public void setMinValue(double value) {
        _minValue = value;
    }

    @Persistable(name="MaxValue")
    public double getMaxValue() {
        return _maxValue;
    }

    public void setMaxValue(double value) {
        _maxValue = value;
    }

    @Persistable(name="InitialValue")
    public double getInitialValue() {
        return _initialValue;
    }

    public void setInitialValue(double value) {
        _initialValue = value;
    }

    protected Multicontroller(DescriptiveName name, double minValue, double maxValue, double initialValue, String modesIds,
                              String iconName, DeviceCategory deviceCategory)    {
        super(name, iconName, deviceCategory);
        setMinValue(minValue);
        setMaxValue(maxValue);
        setInitialValue(initialValue);
        setModesIds(modesIds);
    }

    @Override
    protected ControlType calculateControlType() {
        return ControlType.Multicontroller;
    }
}
