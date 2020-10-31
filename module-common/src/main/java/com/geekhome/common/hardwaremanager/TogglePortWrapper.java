package com.geekhome.common.hardwaremanager;

import com.geekhome.common.PortBase;

public class TogglePortWrapper extends PortBase implements IOutputPort<Boolean> {
    private final ITogglePort _togglePort;
    private boolean _lastState;

    public TogglePortWrapper(ITogglePort togglePort) {
        super(togglePort.getId());
        _togglePort = togglePort;
        _lastState = false;
    }

    @Override
    public void write(Boolean value) throws Exception {
        if (value) {
            _togglePort.toggleOn();
        } else {
            _togglePort.toggleOff();
        }

        _lastState = value;
    }

    @Override
    public Boolean read() {
        return _lastState;
    }
}