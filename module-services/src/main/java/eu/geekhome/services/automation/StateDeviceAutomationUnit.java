package eu.geekhome.services.automation;


import eu.geekhome.services.hardware.Port;
import eu.geekhome.services.hardware.Relay;

import java.util.Map;

public abstract class StateDeviceAutomationUnit implements IDeviceAutomationUnit<State>, IMultistateDeviceAutomationUnit {
    public State _currentState;
    public ControlMode _controlMode;

    private final Map<String, State> _states;

    public StateDeviceAutomationUnit(Map<String, State> states, String initialState) {
        _states = states;
        _currentState = _states.get(initialState);
        _controlMode = ControlMode.Auto;
    }

    @Override
    public void setControlMode(ControlMode value) {
        _controlMode = value;
    }

    @Override
    public ControlMode getControlMode() {
        return _controlMode;
    }

    @Override
    public State getValue() {
        return _currentState;
    }

    protected void setCurrentState(String currentState) {
        _currentState = _states.get(currentState);
    }

    @Override
    public void changeState(String state, ControlMode controlMode, String code, String actor) {
        if (!_currentState.getName().getId().equals(state) || controlMode != getControlMode()) {
            setCurrentState(state);
            setControlMode(controlMode);
        }
    }

    public  void changeStateInternal(String state, ControlMode controlMode) throws Exception {
        changeState(state, controlMode, null, "SYSTEM");
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        return new EvaluationResult(getValue(), _currentState.getName(), isSignaled(), null, getControlMode());
    }

    protected boolean isSignaled() {
        return _currentState.isSignaled();
    }

    protected String getCurrentStateId() {
        return _currentState.getName().getId();
    }

    protected static <T> void changeOutputPortStateIfNeeded(Port<Relay> port, Relay state, boolean invalidate) throws Exception {
        if (port != null && state != null && (invalidate || !state.equals(port.read()))) {
            port.write(state);
        }
    }

    protected static <T> void changeOutputPortStateIfNeeded(Port<Relay> port, Relay state) throws Exception {
        changeOutputPortStateIfNeeded(port, state, false);
    }
}