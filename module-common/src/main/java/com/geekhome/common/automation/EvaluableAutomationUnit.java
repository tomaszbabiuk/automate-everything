package com.geekhome.common.automation;

import com.geekhome.common.configuration.DescriptiveName;

import java.util.Calendar;

public abstract class EvaluableAutomationUnit extends BlocksTargetAutomationUnit implements IEvaluableAutomationUnit {
    private boolean _passed;

    public boolean isPassed() {
        return _passed;
    }

    protected EvaluableAutomationUnit(DescriptiveName name) {
        super(name);
    }

    public boolean evaluate(Calendar now) throws Exception {
        _passed = doEvaluate(now);
        return _passed;
    }

    protected abstract boolean doEvaluate(Calendar now) throws Exception;
}
