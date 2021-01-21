package com.geekhome.common.automation;

import com.geekhome.common.IConnectible;
import com.geekhome.common.configuration.IDevice;
import com.geekhome.common.hardwaremanager.IPort;

import java.util.Calendar;

public abstract class DeviceAutomationUnit<R,D extends IDevice> extends BlocksTargetAutomationUnit implements IDeviceAutomationUnit<R> {
    private final D _device;
    private boolean _isConnected = true;

    public D getDevice() {
        return _device;
    }

    public abstract R getValue();

    protected boolean isConnected() {
        return _isConnected;
    }

    public EvaluationResult buildEvaluationResult() {
        boolean isSignaled = getValue() instanceof Boolean ? (Boolean)getValue() : false;
        return new EvaluationResult(getValue(), getValue().toString(), isSignaled, _isConnected);
    }

    protected DeviceAutomationUnit(D device) {
        super(device.getName());
        _device = device;
    }

    protected boolean arePortsConnected(Calendar now, IPort... ports) {
        for (IPort port : ports) {
            if (port instanceof IConnectible) {
                IConnectible connectible = (IConnectible)port;
                boolean isConnected = connectible.isConnected(now);

                if (!isConnected) {
                    return false;
                }
            }
        }

        return true;
    }

    public abstract IPort[] getUsedPorts();

    @Override
    public void calculate(Calendar now) throws Exception {
        if (arePortsConnected(now, getUsedPorts())) {
            _isConnected = true;
            calculateInternal(now);
        } else {
            _isConnected = false;
        }
    }

    protected abstract void calculateInternal(Calendar now) throws Exception;
}
