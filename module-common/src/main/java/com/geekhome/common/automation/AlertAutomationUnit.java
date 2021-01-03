package com.geekhome.common.automation;

import com.geekhome.common.alerts.IAlertService;
import com.geekhome.common.configuration.Alert;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AlertAutomationUnit extends EvaluableAutomationUnit {
    private final List<IAlertService> _alertServices;
    private final List<String> _alertServicesIds;
    private final Alert _alert;

    public AlertAutomationUnit(Alert alert, SystemInfo systemInfo) {
        super(alert.getName());
        _alert = alert;
        _alertServicesIds = Arrays.asList(alert.getAlertServicesIds().split(","));
        _alertServices = systemInfo.getAlertServices();
    }

    @Override
    protected boolean doEvaluate(Calendar now) throws Exception {
        boolean lastEvaluation = isPassed();
        boolean newEvaluation = checkIfAnyBlockPasses("default");
        if (newEvaluation != lastEvaluation) {
            if (newEvaluation) {
                raiseAlert(now);
            } else {
                clearAlert();
            }
        }
        return newEvaluation;
    }

    private void raiseAlert(Calendar now) throws Exception {
        for (IAlertService alertService : _alertServices) {
            if (alertService.isMandatory() || _alertServicesIds.contains(alertService.getName().getUniqueId())) {
                _alert.setRaisedOn(now.getTime());
                alertService.raiseAlert(_alert);
            }
        }
    }

    private void clearAlert() throws Exception {
        for (IAlertService alertService : _alertServices) {
            if (alertService.isMandatory() || _alertServicesIds.contains(alertService.getName().getUniqueId())) {
                alertService.clearAlert(_alert);
            }
        }
    }
}