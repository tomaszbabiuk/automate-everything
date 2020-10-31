package com.geekhome.common.configuration;

import com.geekhome.common.NamedObject;

public class State extends NamedObject {
    private StateType _type;
    private boolean _codeRequired;

    @Persistable(name="Type")
    public StateType getType() {
        return _type;
    }

    public void setType(StateType value) {
        _type = value;
    }

    @Persistable(name="CodeRequired")
    public boolean isCodeRequired() {
        return _codeRequired;
    }

    public void setCodeRequired(boolean value) {
        _codeRequired = value;
    }

    public State(DescriptiveName name, StateType type, boolean codeRequired) {
        super(name);
        setType(type);
        setCodeRequired(codeRequired);
    }
}
