package com.geekhome.common.hardwaremanager;

public interface IOutputPort<T> extends IInputPort<T> {
    void write(T value) throws Exception;
}
