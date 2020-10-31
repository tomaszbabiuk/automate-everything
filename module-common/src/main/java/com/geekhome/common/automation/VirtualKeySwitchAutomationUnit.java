package com.geekhome.common.automation;

import com.geekhome.common.configuration.KeySwitch;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.common.localization.ILocalizationProvider;

import java.util.Calendar;

public class VirtualKeySwitchAutomationUnit extends MultistateDeviceAutomationUnit<KeySwitch> {
    @Override
    protected boolean isSignaled() {
        return getStateId().equals("on");
    }

    public VirtualKeySwitchAutomationUnit(KeySwitch keySwitch, ILocalizationProvider localizationProvider) throws Exception {
        super(keySwitch, localizationProvider);
        changeStateInternal("off", ControlMode.Manual);
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[0];
    }

    @Override
    protected void calculateInternal(Calendar now) throws Exception {
    }
}