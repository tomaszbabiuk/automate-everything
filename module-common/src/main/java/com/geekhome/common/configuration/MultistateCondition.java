package com.geekhome.common.configuration;

import com.geekhome.common.configuration.ConditionBase;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;

public class MultistateCondition extends ConditionBase {
    private String _deviceId;
    private String _statesIds;
    private String _delayTime;

    @Persistable(name="DeviceId")
    public String getDeviceId() {
        return _deviceId;
    }

    public void setDeviceId(String value) {
        _deviceId = value;
    }

    @Persistable(name = "StatesIds")
    public String getStatesIds() {
        return _statesIds;
    }

    @Persistable(name = "DelayTime")
    public String getDelayTime() {
        return _delayTime;
    }

    public void setDelayTime(String delay) {
        _delayTime = delay;
    }

    @Override
    public String[] getDevicesIds() {
        String[] result = new String[1];
        result[0] = getDeviceId();
        return result;
    }

    public void setStatesIds(String value) {
        _statesIds = value;
    }

    public MultistateCondition(DescriptiveName name, String deviceId, String statesIds, String delayTime) {
        super(name);
        setDeviceId(deviceId);
        setStatesIds(statesIds);
        setDelayTime(delayTime);
    }
}
