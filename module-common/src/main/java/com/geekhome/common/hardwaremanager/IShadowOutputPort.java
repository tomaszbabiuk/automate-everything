package com.geekhome.common.hardwaremanager;

public interface IShadowOutputPort<T> extends IShadowPort, IOutputPort<T> {
    void setTarget(IOutputPort<T> target);
}
