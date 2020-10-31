package com.geekhome.common.automation;

import com.geekhome.common.INamedObject;
import java.util.Calendar;

public interface ICalculableAutomationUnit extends INamedObject {
    void calculate(Calendar now) throws Exception;
}
