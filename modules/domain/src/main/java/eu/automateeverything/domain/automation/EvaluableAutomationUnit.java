package eu.automateeverything.domain.automation;

import java.util.Calendar;

public interface EvaluableAutomationUnit {
    boolean evaluate(Calendar now) throws Exception;
    boolean isPassed();
}
