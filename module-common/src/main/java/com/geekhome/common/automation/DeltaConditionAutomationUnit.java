package com.geekhome.common.automation;

import com.geekhome.common.EqualityOperator;
import com.geekhome.common.configuration.DeltaCondition;

import java.util.Calendar;

public class DeltaConditionAutomationUnit extends EvaluableAutomationUnit {
    private boolean _previousEvaluation;
    private final DeltaCondition _condition;
    private final IDeviceAutomationUnit _firstDEviceUnit;
    private final IDeviceAutomationUnit _secondDEviceUnit;

    public DeltaConditionAutomationUnit(DeltaCondition condition, MasterAutomation masterAutomation) throws Exception {
        super(condition.getName());
        _condition = condition;
        _firstDEviceUnit = masterAutomation.findDeviceUnit(condition.getFirstDeviceId());
        _secondDEviceUnit = masterAutomation.findDeviceUnit(condition.getSecondDeviceId());
        _previousEvaluation = false;
    }

    @Override
    protected boolean doEvaluate(Calendar now) {
        if ((_firstDEviceUnit.getValue() == null) || (_secondDEviceUnit.getValue() == null)) {
            return false;
        }

        double correction = _previousEvaluation ? _condition.getHysteresis() : 0;
        double difference = (double)_firstDEviceUnit.getValue() - (double)_secondDEviceUnit.getValue();

        if (_condition.getOperator() == EqualityOperator.GreaterOrEqual) {
            _previousEvaluation = difference >= _condition.getDelta() - correction;
        } else {
            _previousEvaluation = difference < _condition.getDelta() + correction;
        }

        return _previousEvaluation;
    }
}

