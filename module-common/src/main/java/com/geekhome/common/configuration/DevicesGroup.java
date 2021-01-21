package com.geekhome.common.configuration;

import com.geekhome.common.NamedObject;

public class DevicesGroup extends NamedObject {
    private final CollectorCollection<IDevice> _devices;

    public DevicesGroup(DescriptiveName name) {
        super(name);
        _devices = new CollectorCollection<>();
    }

    @Persistable(name="Devices")
    public CollectorCollection<IDevice> getDevices() {
        return _devices;
    }

}
