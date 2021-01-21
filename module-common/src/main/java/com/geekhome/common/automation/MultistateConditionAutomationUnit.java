package com.geekhome.common.automation;

import com.geekhome.common.NamedObject;
import com.geekhome.common.configuration.Duration;
import com.geekhome.common.configuration.MultistateCondition;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MultistateConditionAutomationUnit extends EvaluableAutomationUnit {
    private final List<String> _states;
    private final long _durationMs;
    private boolean _prevValue;
    private final IMultistateDeviceAutomationUnit _deviceUnit;
    private boolean _delaying;
    private long _evaluatedAt;

    public MultistateConditionAutomationUnit(MultistateCondition condition, MasterAutomation automation) throws Exception {
        super(condition.getName());
        _states = Arrays.asList(condition.getStatesIds().split(","));
        _deviceUnit = (IMultistateDeviceAutomationUnit) automation.findDeviceUnit(condition.getDeviceId());
        _durationMs = Duration.parse(condition.getDelayTime());
        _prevValue = false;
    }

    protected boolean doEvaluate(Calendar now) {
        NamedObject currentState = _deviceUnit.getState();
        boolean currentValue =  _states.contains(currentState.getName().getUniqueId());
        _delaying = currentValue;
        if (currentValue && !_prevValue) {
            _evaluatedAt = Calendar.getInstance().getTimeInMillis();
        }

        _prevValue = currentValue;

        return _delaying && _evaluatedAt + _durationMs < now.getTimeInMillis();
    }
}