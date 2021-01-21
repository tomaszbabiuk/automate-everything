package com.geekhome.common.automation;

import com.geekhome.common.configuration.KeySwitch;
import com.geekhome.common.hardwaremanager.IInputPort;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.common.localization.ILocalizationProvider;

import java.util.Calendar;

public class KeySwitchAutomationUnit extends VirtualKeySwitchAutomationUnit {
    private final IInputPort<Boolean> _inputPort;

    public KeySwitchAutomationUnit(IInputPort<Boolean> inputPort, KeySwitch device, ILocalizationProvider localizationProvider) throws Exception {
          super(device, localizationProvider);
        _inputPort = inputPort;
        setControlMode(ControlMode.Auto);
    }

    @Override
    protected boolean isSignaled() {
        return _inputPort.read();
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        if (_inputPort.read()) {
            changeStateInternal("on", ControlMode.Auto);
        } else {
            changeStateInternal("off", ControlMode.Auto);
        }
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _inputPort };
    }
}