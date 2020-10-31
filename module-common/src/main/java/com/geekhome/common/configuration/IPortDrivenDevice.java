package com.geekhome.common.configuration;

import com.geekhome.common.INamedObject;

public interface IPortDrivenDevice extends INamedObject {
    String getPortId();
}
