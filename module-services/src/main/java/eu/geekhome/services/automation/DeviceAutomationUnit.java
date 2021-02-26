package eu.geekhome.services.automation;


import eu.geekhome.services.hardware.Connectible;
import eu.geekhome.services.hardware.Port;

import java.util.Calendar;

public abstract class DeviceAutomationUnit<R> extends BlocksTargetAutomationUnit implements IDeviceAutomationUnit<R> {
    private boolean _isConnected = true;

    protected boolean isConnected() {
        return _isConnected;
    }

    public abstract EvaluationResult buildEvaluationResult();

    protected boolean arePortsConnected(Calendar now, Port... ports) {
        for (Port port : ports) {
            if (port instanceof Connectible) {
                Connectible connectible = (Connectible)port;
                boolean isConnected = connectible.checkIfConnected(now.getTimeInMillis());

                if (!isConnected) {
                    return false;
                }
            }
        }

        return true;
    }

    public abstract Port[] getUsedPorts();

    @Override
    public void calculate(Calendar now) throws Exception {
        //if (arePortsConnected(now, getUsedPorts())) {
        //    _isConnected = true;
            calculateInternal(now);
        //} else {
        //    _isConnected = false;
        //}
    }

    protected abstract void calculateInternal(Calendar now) throws Exception;
}
