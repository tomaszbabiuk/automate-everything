package com.geekhome.common.configuration;

import com.geekhome.common.NamedObject;

public class ConditionBase extends NamedObject implements ICondition {
    public ConditionBase(DescriptiveName name) {
        super(name);
    }

    @Override
    public String[] getDevicesIds() {
        return new String[0];
    }
}
