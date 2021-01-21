package com.geekhome.common.automation;

import com.geekhome.common.DateTimeInfo;
import com.geekhome.common.configuration.Persistable;

public class SystemStatus {
    private final DateTimeInfo _date;
    private final int _operationMode;
    private final String _userName;
    private final boolean _isLicenceAccepted;

    @Persistable(name="Date")
    public DateTimeInfo getDate() {
        return _date;
    }

    @Persistable(name="OperationMode")
    public int getOperationMode() {
        return _operationMode;
    }

    @Persistable(name="UserName")
    public String getUserName() {
        return _userName;
    }

    @Persistable(name="IsLicenseAccepted")
    public boolean isLicenseAccepted() {
        return _isLicenceAccepted;
    }

    public SystemStatus(String userName, int operationMode, boolean isLicenceAccepted) {
        _userName = userName;
        _operationMode = operationMode;
        _date = new DateTimeInfo();
        _isLicenceAccepted = isLicenceAccepted;
    }
}