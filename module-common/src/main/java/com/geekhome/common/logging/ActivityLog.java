package com.geekhome.common.logging;

import com.geekhome.common.configuration.Persistable;


public class ActivityLog<T> extends TimedLog {
    private String _source;
    private  T _value;
    private  T _prevValue;

    public ActivityLog(String source, T value, T prevValue) {
        super(LogLevel.Activity);
        setSource(source);
        setValue(value);
        setPrevValue(prevValue);
    }

    @Persistable(name="Source")
    public String getSource() {
        return _source;
    }

    public void setSource(String value) {
        _source = value;
    }

    @Persistable(name="PrevValue")
    public T getPrevValue() {
        return _prevValue;
    }

    public void setPrevValue(T value) {
        _prevValue = value;
    }

    @Persistable(name="Value")
    public T getValue() {
        return _value;
    }

    public void setValue(T value) {
        _value = value;
    }

    @Override
    public String toString() {
        return String.format("%04d/%02d/%02d %02d:%02d:%02d.%02d [ACTIVITY] %s  -> %s", getYear(), getMonth() + 1, getDay(), getHour(), getMinute(), getSecond(), getMillisecond(), getSource(), getValue().toString());
    }

    public String toShortString() {
        return String.format("%02d:%02d:%02d.%03d %s", getHour(), getMinute(), getSecond(), getMillisecond(), getValue().toString());
    }

}
