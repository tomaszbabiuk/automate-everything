package com.geekhome.common.automation;

import com.geekhome.common.EqualityOperator;
import com.geekhome.common.configuration.ValueCondition;

import java.util.Calendar;

class ValueConditionAutomationUnit extends EvaluableAutomationUnit {
    private boolean _previousEvaluation;
    private ValueCondition _condition;

    private DeviceAutomationUnit _deviceUnit;

    ValueConditionAutomationUnit(ValueCondition condition, MasterAutomation masterAutomation) throws Exception {
        super(condition.getName());
        _condition = condition;
        _deviceUnit = (DeviceAutomationUnit) masterAutomation.findDeviceUnit(condition.getDeviceId());
        _previousEvaluation = false;
    }

    @Override
    protected boolean doEvaluate(Calendar now) {
        if (_deviceUnit.getValue() == null) {
            return false;
        }

        double correction = _previousEvaluation ? _condition.getHysteresis() : 0;

        Double deviceValueAsDouble = null;
        if (_deviceUnit.getValue() instanceof Integer) {
            deviceValueAsDouble= (Integer)_deviceUnit.getValue() * 1.0;
        } else {
            deviceValueAsDouble= (Double)_deviceUnit.getValue();
        }

        if (_condition.getOperator() == EqualityOperator.GreaterOrEqual) {
            _previousEvaluation = deviceValueAsDouble >= _condition.getValue() - correction;
        } else {
            _previousEvaluation = deviceValueAsDouble < _condition.getValue() + correction;
        }

        return _previousEvaluation;
    }
}
