package com.geekhome.common.configuration;

import com.geekhome.common.INamedObject;

public interface ICondition extends INamedObject {
    String[] getDevicesIds();
}
