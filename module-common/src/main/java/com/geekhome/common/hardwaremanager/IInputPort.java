package com.geekhome.common.hardwaremanager;

public interface IInputPort<T> extends IPort {
    T read();
}
