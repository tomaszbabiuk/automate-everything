package com.geekhome.common.automation;

import com.geekhome.common.configuration.GeofenceCondition;

import java.util.Calendar;
import java.util.Hashtable;

public class GeofenceConditionAutomationUnit extends EvaluableAutomationUnit {
    private final Hashtable<String, Boolean> _rangeEvaluations;

    public GeofenceConditionAutomationUnit(GeofenceCondition condition) {
        super(condition.getName());
        _rangeEvaluations = new Hashtable<>();
        if (condition.getGeofencesIds() != null && !condition.getGeofencesIds().isEmpty()) {
            for (String geofenceId : condition.getGeofencesIds().split(",")) {
                _rangeEvaluations.put(geofenceId, false);
            }
        }
    }

    @Override
    protected boolean doEvaluate(Calendar now) {
        for (boolean rangeEvaluation : _rangeEvaluations.values()) {
            if (!rangeEvaluation) {
                return false;
            }
        }
        return true;
    }

    public void geofenceEnter(String geofenceId) {
        updateGeofenceEvaluation(geofenceId, true);
    }

    public void geofenceExit(String geofenceId) {
        updateGeofenceEvaluation(geofenceId, false);
    }

    private void updateGeofenceEvaluation(String geofenceId, boolean evaluation) {
        if (_rangeEvaluations.containsKey(geofenceId)) {
            _rangeEvaluations.put(geofenceId, evaluation);
        }
    }
}