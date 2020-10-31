package com.geekhome.common.logging;

import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.common.configuration.JSONAwareBase;

public class ErrorLog extends JSONAwareBase {
    private JSONArrayList<TimedLog> _logs;

    public ErrorLog(JSONArrayList<TimedLog> logs) {
        _logs = logs;
    }

    @Persistable(name="Logs")
    public JSONArrayList getLogs() {
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
