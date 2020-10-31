package com.geekhome.common.logging;

import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.JSONAwareBase;

import java.util.Calendar;

public class TimedLog extends JSONAwareBase {
    private int _day;
    private int _month;
    private int _year;
    private int _hour;
    private int _minute;
    private int _second;
    private int _millisecond;
    private LogLevel _level;

    public TimedLog(LogLevel level) {
        Calendar calendar = Calendar.getInstance();
        setDay(calendar.get(Calendar.DAY_OF_MONTH));
        setMonth(calendar.get(Calendar.MONTH));
        setYear(calendar.get(Calendar.YEAR));
        setHour(calendar.get(Calendar.HOUR_OF_DAY));
        setMinute(calendar.get(Calendar.MINUTE));
        setSecond(calendar.get(Calendar.SECOND));
        setMillisecond(calendar.get(Calendar.MILLISECOND));
        setLevel(level);
    }

    @Persistable(name="Level")
    public LogLevel getLevel() {
        return _level;
    }

    public void setLevel(LogLevel value) {
        _level = value;
    }

    @Persistable(name="Day")
    public int getDay() {
        return _day;
    }

    public void setDay(int value) {
        _day = value;
    }

    @Persistable(name="Month")
    public int getMonth() {
        return _month;
    }

    public void setMonth(int value) {
        _month = value;
    }

    @Persistable(name="Year")
    public int getYear() {
        return _year;
    }

    public void setYear(int value) {
        _year = value;
    }

    @Persistable(name="Hour")
    public int getHour() {
        return _hour;
    }

    public void setHour(int value) {
        _hour = value;
    }

    @Persistable(name="Minute")
    public int getMinute() {
        return _minute;
    }

    public void setMinute(int value) {
        _minute = value;
    }

    @Persistable(name="Second")
    public int getSecond() {
        return _second;
    }

    public void setSecond(int value) {
        _second = value;
    }

    @Persistable(name="Millisecond")
    public int getMillisecond() {
        return _millisecond;
    }

    public void setMillisecond(int value) {
        _millisecond = value;
    }
}
