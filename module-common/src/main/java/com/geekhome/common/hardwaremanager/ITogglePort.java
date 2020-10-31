package com.geekhome.common.hardwaremanager;

public interface ITogglePort extends IPort {
    void toggleOn() throws Exception;
    void toggleOff() throws Exception;
}
