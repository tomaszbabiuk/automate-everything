package eu.geekhome.services.automation;


import eu.geekhome.services.localization.ResourceWithId;

public class State {
    private StateType _type;
    private boolean _codeRequired;
    private boolean _signaled;
    private ResourceWithId _name;

    public StateType getType() {
        return _type;
    }

    public boolean isCodeRequired() {
        return _codeRequired;
    }

    public void setCodeRequired(boolean value) {
        _codeRequired = value;
    }

    public State(ResourceWithId name, StateType type, boolean signaled, boolean codeRequired) {
        _name = name;
        _type = type;
        _signaled = signaled;
        _codeRequired = codeRequired;
    }

    public boolean isSignaled() {
        return _signaled;
    }

    public ResourceWithId getName() {
        return _name;
    }
}
