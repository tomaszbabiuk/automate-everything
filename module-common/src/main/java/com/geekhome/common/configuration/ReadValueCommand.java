package com.geekhome.common.configuration;

import com.geekhome.common.configuration.CommandBase;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;

public class ReadValueCommand extends CommandBase {
    private String _devicesIds;

    @Persistable(name="DevicesIds")
    public String getDevicesIds() {
        return _devicesIds;
    }

    public void setDevicesIds(String value) {
        _devicesIds = value;
    }

    public ReadValueCommand(DescriptiveName name, String devicesIds) {
        super(name);
        setDevicesIds(devicesIds);
    }
}
