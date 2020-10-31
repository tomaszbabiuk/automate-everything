package com.geekhome.common;

public interface IMonitorable extends INamedObject {
    boolean isOperational();
    void start();
    String getStatus();
}
