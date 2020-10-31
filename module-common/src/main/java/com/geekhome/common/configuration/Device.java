package com.geekhome.common.configuration;

import com.geekhome.common.*;

public abstract class Device extends NamedObject implements IDevice {
    private String _iconName;
    private DeviceCategory _deviceCategory;

    @Persistable(name="IconName")
    public String getIconName() {
        return _iconName;
    }

    public void setIconName(String value) {
        _iconName = value;
    }

    @Persistable(name="ControlType")
    public ControlType getControlType() {
        return calculateControlType();
    }

    protected abstract ControlType calculateControlType();

    @Persistable(name="DeviceCategory")
    public DeviceCategory getCategory() { return _deviceCategory; }

    public void setDeviceCategory(DeviceCategory deviceCategory) {
        _deviceCategory = deviceCategory;
    }

    protected Device(DescriptiveName name, String iconName, DeviceCategory deviceCategory) {
        super(name);
        setName(name);
        setIconName(iconName);
        setDeviceCategory(deviceCategory);
    }
}