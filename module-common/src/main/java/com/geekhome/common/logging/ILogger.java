package com.geekhome.common.logging;

import com.geekhome.common.configuration.JSONArrayList;

public interface ILogger {
    JSONArrayList<TimedLog> getActivities();
    JSONArrayList<ErrorLog> getErrors();

    void verbose(String message);
    void debug(String message);
    void info(String message);
    void warning(String message);
    void warning(String message, Throwable exception);
    void error(String message, Throwable exception);
    <T> void activity(String source, T value, T prevValue);
    <T> void valueChange(String deviceId, T value);

    void clearActivities();
    int getErrorsCount();
}
