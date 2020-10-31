package com.geekhome.common.automation;

import com.geekhome.common.NamedObject;
import com.geekhome.common.configuration.State;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.configuration.MultistateDevice;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.common.configuration.CollectorCollection;
import com.geekhome.common.configuration.ObjectNotFoundException;

public abstract class MultistateDeviceAutomationUnit<D extends MultistateDevice> extends DeviceAutomationUnit<String, D> implements IMultistateDeviceAutomationUnit {
    public NamedObject _state;
    public ControlMode _controlMode;
    private ILogger _logger = LoggingService.getLogger();

    private CollectorCollection<State> _states;
    private ILocalizationProvider _localizationProvider;

    protected ILocalizationProvider getLocalizationProvider() {
        return _localizationProvider;
    }

    public MultistateDeviceAutomationUnit(D device, ILocalizationProvider localizationProvider) {
        super(device);
        _localizationProvider = localizationProvider;
        _states = device.buildStates(localizationProvider);
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
    public String getValue() {
        return getStateId();
    }

    @Override
    public NamedObject getState() {
        return _state;
    }

    protected void setState(String state) throws ObjectNotFoundException {
        _state = _states.find(state);
    }

    protected CollectorCollection<State> getStates() {
        return _states;
    }

    @Override
    public void changeState(String state, ControlMode controlMode, String code, String actor) throws Exception {
        if (getStateId() == null || !getStateId().equals(state) || controlMode != getControlMode()) {
            _logger.valueChange(getDevice().getName().getUniqueId(), state);
            setState(state);
            setControlMode(controlMode);
        }
    }

    public  void changeStateInternal(String state, ControlMode controlMode) throws Exception {
        changeState(state, controlMode, null, "SYSTEM");
    }

    public String getStateId() {
        return getState() == null ? null : getState().getName().getUniqueId();
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        return new EvaluationResult(getValue(), getState().getName().getName(), isSignaled(), isConnected(), null, getControlMode(), false);
    }

    protected abstract boolean isSignaled();

    protected static <T> void changeOutputPortStateIfNeeded(IOutputPort<T> port, T state, boolean invalidate) throws Exception {
        if (port != null && state != null && (invalidate || !state.equals(port.read()))) {
            port.write(state);
        }
    }

    protected static <T> void changeOutputPortStateIfNeeded(IOutputPort<T> port, T state) throws Exception {
        changeOutputPortStateIfNeeded(port, state, false);
    }
}