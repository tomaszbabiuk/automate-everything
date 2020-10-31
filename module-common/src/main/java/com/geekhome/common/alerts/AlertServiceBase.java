package com.geekhome.common.alerts;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.NamedObject;
import com.geekhome.common.configuration.Persistable;

public abstract class AlertServiceBase extends NamedObject implements IAlertService {
    private boolean _isMandatory;

    public AlertServiceBase(DescriptiveName name, boolean isMandatory) {
        super(name);
        _isMandatory = isMandatory;
    }

    @Override
    @Persistable(name = "IsMandatory")
    public boolean isMandatory() {
        return _isMandatory;
    }
}
