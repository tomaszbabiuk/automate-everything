package com.geekhome.common.automation;

import com.geekhome.common.NamedObject;

public interface IMultistateDeviceAutomationUnit {
    void changeState(String state, ControlMode controlMode, String code, String actor) throws Exception;
    NamedObject getState();
    void setControlMode(ControlMode value);
    ControlMode getControlMode();
}
