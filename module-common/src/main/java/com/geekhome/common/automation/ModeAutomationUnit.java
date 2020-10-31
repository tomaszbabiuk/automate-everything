package com.geekhome.common.automation;

import com.geekhome.common.IPrioritized;
import com.geekhome.common.configuration.Mode;

import java.util.Calendar;

public class ModeAutomationUnit extends EvaluableAutomationUnit implements IPrioritized {
    private Mode _mode;

    public Mode getMode() {
        return _mode;
    }

    private void setMode(Mode value) {
        _mode = value;
    }

    public ModeAutomationUnit(Mode mode) {
        super(mode.getName());
        setMode(mode);
    }

    public int getPriority() {
        return getMode().getPriority();
    }

    @Override
    protected boolean doEvaluate(Calendar now) throws Exception {
        return checkIfAnyBlockPasses("default");
    }
}