package com.geekhome.common.logging;

import com.geekhome.common.configuration.Persistable;


public class EventLog extends TimedLog{
    private String _message;
    private String _details;

    public EventLog(String message, String details, LogLevel level) {
        super(level);
        setMessage(message);
        setDetails(details);
    }

    @Persistable(name="Message")
    public String getMessage() {
        return _message;
    }

    public void setMessage(String value) {
        _message = value;
    }

    @Persistable(name="Details")
    public String getDetails() {
        return _details;
    }

    public void setDetails(String value) {
        _details = value;
    }

    @Override
    public String toString() {
        if (getDetails()  == null) {
            return String.format("%04d/%02d/%02d %d:%d:%d.%d [%s] %s", getYear(), getMonth() + 1, getDay(), getHour(), getMinute(), getSecond(), getMillisecond(),
                    getLevel().getName(), getMessage());
        }
        return String.format("%04d/%02d/%02d %d:%d:%d.%d [%s] %s \r\n [%s]", getYear(), getMonth() + 1, getDay(), getHour(), getMinute(), getSecond(), getMillisecond(),
                getLevel().getName(), getMessage(), getDetails());
    }
}
