package com.geekhome.common;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;

public abstract class MonitorableBase extends NamedObject implements IMonitorable {
    private boolean _isOperational;
    private String _status;

    public MonitorableBase(DescriptiveName name, boolean isOperational, String status) {
        super(name);
        _isOperational = isOperational;
        _status = status;
    }

    @Override
    @Persistable(name="IsOperational")
    public boolean isOperational() {
        return _isOperational;
    }

    @Override
    @Persistable(name="Status")
    public String getStatus() {
        return _status;
    }

    public void updateStatus(boolean isOperational, String status) {
        _isOperational = isOperational;
        _status = status;
    }
}
