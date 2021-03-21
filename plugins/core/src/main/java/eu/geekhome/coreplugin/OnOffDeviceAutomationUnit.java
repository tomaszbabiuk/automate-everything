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

    public OnOffDeviceAutomationUnit(Map<String, State> states, String initialState, Port<Relay> controlPort) {
        super(states, initialState);
        _controlPort = controlPort;
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
}