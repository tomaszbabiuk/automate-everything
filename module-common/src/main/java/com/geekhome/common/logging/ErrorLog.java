package com.geekhome.common.logging;

import com.geekhome.common.configuration.Persistable;

import java.util.List;

public class ErrorLog {
    private List<TimedLog> _logs;

    public ErrorLog(List<TimedLog> logs) {
        _logs = logs;
    }

    @Persistable(name="Logs")
    public List getLogs() {
        return _logs;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("(...)");
        result.append(System.lineSeparator());
        for (TimedLog pastLog: _logs) {
            result.append(pastLog.toString());
            result.append(System.lineSeparator());
        }

        return result.toString();
    }
}
