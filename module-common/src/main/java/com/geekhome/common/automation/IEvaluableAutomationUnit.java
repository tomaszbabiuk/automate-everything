package com.geekhome.common.automation;

import com.geekhome.common.INamedObject;

import java.util.Calendar;

public interface IEvaluableAutomationUnit extends INamedObject, IBlocksTargetAutomationUnit {
    boolean evaluate(Calendar now) throws Exception;
    boolean isPassed();
}
