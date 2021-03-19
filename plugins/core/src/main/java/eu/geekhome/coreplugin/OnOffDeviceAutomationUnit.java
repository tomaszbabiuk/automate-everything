package eu.geekhome.coreplugin;

import eu.geekhome.services.automation.State;
import eu.geekhome.services.automation.StateDeviceAutomationUnit;
import eu.geekhome.services.hardware.Port;
import eu.geekhome.services.hardware.Relay;
import org.jetbrains.annotations.NotNull;

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
    public void calculate(Calendar now) throws Exception {
        if (getCurrentStateId().equals("on")) {
            changeOutputPortStateIfNeeded(_controlPort, new Relay(true));
        } else if (getCurrentStateId().equals("off")) {
            changeOutputPortStateIfNeeded(_controlPort, new Relay(false));
        }
    }

    @NotNull
    @Override
    public Class<State> getValueType() {
        return State.class;
    }
}