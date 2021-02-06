package eu.geekhome.services.automation;

import java.util.Calendar;

public interface IEvaluableAutomationUnit extends IBlocksTargetAutomationUnit {
    boolean evaluate(Calendar now) throws Exception;
    boolean isPassed();
}
