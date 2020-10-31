package com.geekhome.common.automation;

import com.geekhome.common.configuration.DescriptiveName;

import java.util.Calendar;

public class AlwaysOnAutomationUnit extends EvaluableAutomationUnit {

    public AlwaysOnAutomationUnit() {
        super(new DescriptiveName("alwayson", "alwayson"));
    }

    @Override
    protected boolean doEvaluate(Calendar now) {
        return true;
    }
}