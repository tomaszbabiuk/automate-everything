package com.geekhome.common.automation;

import com.geekhome.common.INamedObject;
import com.geekhome.common.configuration.IDevice;

public interface IDeviceAutomationUnit<R> extends INamedObject, IBlocksTargetAutomationUnit, ICalculableAutomationUnit {
    R getValue();
    IDevice getDevice();
    EvaluationResult buildEvaluationResult();
}

