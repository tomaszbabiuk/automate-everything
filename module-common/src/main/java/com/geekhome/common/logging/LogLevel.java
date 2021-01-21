package com.geekhome.common.logging;

public enum LogLevel
{
    Verbose(0, "VERBOSE"),
    Debug(1, "DEBUG"),
    Info(2, "INFO"),
    Warning(3, "WARNING"),
    Error(4, "ERROR"),
    Alert(5, "ALERT"),
    Activity(6, "ACTIVITY"),
    Value(7, "VALUE");

    private final int _index;
    private final String _name;

    LogLevel(int index, String name) {
        _index = index;
        _name = name;
    }

    @Override
    public String toString() {
        return String.valueOf(_index);
    }

    public Integer toInt() {
        return _index;
    }

    public String getName() {
        return _name;
    }
}