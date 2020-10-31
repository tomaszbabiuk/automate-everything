package com.geekhome.common.configuration;

public class ChangeStateCommand extends CommandBase {
    private String _deviceId;
    private String _stateId;

    @Persistable(name="DeviceId")
    public String getDeviceId() {
        return _deviceId;
    }

    public void setDeviceId(String value) {
        _deviceId = value;
    }

    @Persistable(name="StateId")
    public String getStateId() {
        return _stateId;
    }

    public void setStateId(String value) {
        _stateId = value;
    }

    public ChangeStateCommand(DescriptiveName name, String deviceId, String stateId) {
        super(name);
        setDeviceId(deviceId);
        setStateId(stateId);
    }
}
