package com.geekhome.common.configuration;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;

public class GeofenceCondition extends ConditionBase {
    private String _geofencesIds;

    @Persistable(name = "GeofencesIds")
    public String getGeofencesIds() {
        return _geofencesIds;
    }

    public void setGeofencesIds(String geofencesIds) {
        _geofencesIds = geofencesIds;
    }

    public GeofenceCondition(DescriptiveName name, String geofencesIds) {
        super(name);
        setGeofencesIds(geofencesIds);
    }
}