package com.geekhome.common.alerts;

import com.geekhome.common.INamedObject;
import com.geekhome.common.configuration.Alert;

public interface IAlertService extends INamedObject {
    boolean isMandatory();
    void raiseAlert(Alert alert) throws Exception;
    void clearAlert(Alert alert) throws Exception;
}
