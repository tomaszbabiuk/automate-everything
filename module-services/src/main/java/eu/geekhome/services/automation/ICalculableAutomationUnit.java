package eu.geekhome.services.automation;

import java.util.Calendar;

public interface ICalculableAutomationUnit {
    void calculate(Calendar now) throws Exception;
}
