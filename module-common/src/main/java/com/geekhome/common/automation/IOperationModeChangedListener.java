package com.geekhome.common.automation;

import com.geekhome.common.OperationMode;

public interface IOperationModeChangedListener {
    void onChanged(OperationMode operationMode) throws Exception;
}
