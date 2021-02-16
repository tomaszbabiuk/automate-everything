package eu.geekhome.coreplugin;

import eu.geekhome.services.automation.ControlMode;
import eu.geekhome.services.automation.State;
import eu.geekhome.services.automation.StateDeviceAutomationUnit;
import eu.geekhome.services.hardware.Port;
import eu.geekhome.services.hardware.Relay;

import java.util.Calendar;
import java.util.Map;

public class OnOffDeviceAutomationUnit extends StateDeviceAutomationUnit {
    private final Port<Relay> _controlPort;
    private Calendar _lastSwitchingOnTime = null;

    protected Calendar getLastSwitchingOnTime() {
        return _lastSwitchingOnTime;
    }

    protected void setLastSwitchingOnTime(Calendar value) {
        _lastSwitchingOnTime = value;
    }

    public OnOffDeviceAutomationUnit(Map<String, State> states, String initialState, Port<Relay> controlPort) {
        super(states, initialState);
        _controlPort = controlPort;
        controlPort.write(new Relay(true));
        setLastSwitchingOnTime(Calendar.getInstance());
        setCurrentState("off");
    }

    @Override
    public Port[] getUsedPorts() {
        return new Port[] {_controlPort};
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        if (getControlMode() == ControlMode.Auto) {
            if (checkIfAnyBlockPasses("on")) {
                if (!_controlPort.read().getValue()) {
                    setLastSwitchingOnTime(Calendar.getInstance());
                }
                changeStateInternal("on", ControlMode.Auto);
            } else {
                changeStateInternal("off", ControlMode.Auto);
            }
        }

        if (getCurrentStateId().equals("on")) {
            changeOutputPortStateIfNeeded(_controlPort, new Relay(true));
        } else if (getCurrentStateId().equals("off")) {
            changeOutputPortStateIfNeeded(_controlPort, new Relay(false));
        }
    }
}