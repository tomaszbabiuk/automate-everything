package com.geekhome.common.commands;

import com.geekhome.common.configuration.IDevice;
import com.geekhome.common.automation.IDeviceAutomationUnit;

import java.util.ArrayList;

public class CommandResult {
    private String _output;
    private ArrayList<IDeviceAutomationUnit> _automationUnits;
    private boolean _codeRequired;
    private IDevice _controlledDevice;

    private CommandResult(String output, ArrayList<IDeviceAutomationUnit> automationUnits, boolean codeRequired, IDevice controlledDevice) {
        _output = output;
        _automationUnits = automationUnits;
        _codeRequired = codeRequired;
        _controlledDevice = controlledDevice;
    }

    public CommandResult(String output, ArrayList<IDeviceAutomationUnit> automationUnits) {
        this(output, automationUnits, false, null);
    }

    public CommandResult(String output, boolean codeRequired, IDevice controlledDevice) {
        this(output, null, codeRequired, controlledDevice);
    }

    public CommandResult(String output) {
        this(output, null, false, null);
    }

    public String getOutput() {
        return _output;
    }

    public ArrayList<IDeviceAutomationUnit> getAutomationUnits() {
        return _automationUnits;
    }

    public boolean isCodeRequired() {
        return _codeRequired;
    }

    public IDevice getControlledDevice() {
        return _controlledDevice;
    }
}
