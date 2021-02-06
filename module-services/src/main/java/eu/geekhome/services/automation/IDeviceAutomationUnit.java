package eu.geekhome.services.automation;

public interface IDeviceAutomationUnit<R> extends IBlocksTargetAutomationUnit, ICalculableAutomationUnit {
    R getValue();
    IDevice getDevice();
    EvaluationResult buildEvaluationResult();
}

