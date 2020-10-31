package com.geekhome.common.automation;

import com.geekhome.common.configuration.MultistateDevice;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.common.localization.ILocalizationProvider;

import java.util.Calendar;

public class SwitchableAutomationUnit<D extends MultistateDevice> extends MultistateDeviceAutomationUnit<D> {
    private IOutputPort<Boolean> _outputPort;

    protected IOutputPort<Boolean> getOutputPort() {
        return _outputPort;
    }

    public SwitchableAutomationUnit(IOutputPort<Boolean> outputPort, D device, ILocalizationProvider localizationProvider) throws Exception {
        super(device, localizationProvider);
        _outputPort = outputPort;
        changeStateInternal("off", ControlMode.Auto);
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _outputPort };
    }

    @Override
    protected void calculateInternal(Calendar now) throws Exception {
        if (getStateId().equals("on")) {
            changeOutputPortStateIfNeeded(_outputPort, true);
        } else if (getStateId().equals("off")) {
            changeOutputPortStateIfNeeded(_outputPort, false);
        }
    }

    @Override
    protected boolean isSignaled() {
        return _outputPort.read();
    }
}
