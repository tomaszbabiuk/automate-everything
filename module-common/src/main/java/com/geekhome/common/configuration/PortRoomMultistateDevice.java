package com.geekhome.common.configuration;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.DeviceCategory;
import com.geekhome.common.configuration.IPortDrivenDevice;
import com.geekhome.common.configuration.IRoomDevice;
import com.geekhome.common.configuration.Persistable;

public abstract class PortRoomMultistateDevice extends MultistateDevice implements IRoomDevice, IPortDrivenDevice {
    private String _roomId;
    private String _portId;

    @Persistable(name = "RoomId")
    public String getRoomId() {
        return _roomId;
    }

    public void setRoomId(String value) {
        _roomId = value;
    }

    @Persistable(name = "PortId")
    public String getPortId() {
        return _portId;
    }

    public void setPortId(String value) {
        _portId = value;
    }

    public PortRoomMultistateDevice(DescriptiveName name, String iconName, DeviceCategory deviceCategory, String portId, String roomId) {
        super(name, iconName, deviceCategory);
        setPortId(portId);
        setRoomId(roomId);
    }
}
