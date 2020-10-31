package com.geekhome.common.hardwaremanager;

public interface IShadowInputPort<T> extends IShadowPort, IInputPort<T> {
    void setTarget(IInputPort<T> target);
}
