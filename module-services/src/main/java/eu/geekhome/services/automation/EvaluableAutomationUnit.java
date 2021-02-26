package eu.geekhome.services.automation;

import java.util.Calendar;

public abstract class EvaluableAutomationUnit extends BlocksTargetAutomationUnit implements IEvaluableAutomationUnit {
    private boolean _passed;
    private long _lastEvaluationTime;

    public boolean isPassed() {
        return _passed;
    }

    public boolean evaluate(Calendar now) throws Exception {
        if (_lastEvaluationTime != now.getTimeInMillis()) {
            _passed = doEvaluate(now);
            _lastEvaluationTime = now.getTimeInMillis();
        }

        return _passed;
    }

    protected abstract boolean doEvaluate(Calendar now) throws Exception;
}
