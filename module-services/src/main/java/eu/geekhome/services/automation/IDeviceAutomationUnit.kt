package eu.geekhome.services.automation;

public interface IDeviceAutomationUnit<R> extends ICalculableAutomationUnit {
    R getValue();
    EvaluationResult buildEvaluationResult();
}

