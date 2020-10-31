package com.geekhome.common;

import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.JSONAwareBase;

import java.util.Calendar;

public class DateTimeInfo extends JSONAwareBase {
    private Calendar _dateTime;

    @Persistable(name="Year")
    public int getYear() {
        return _dateTime.get(Calendar.YEAR);
    }

    @Persistable(name="Month")
    public int getMonth() {
        return _dateTime.get(Calendar.MONTH) + 1;
    }

    @Persistable(name="Day")
    public int getDay() {
        return _dateTime.get(Calendar.DAY_OF_MONTH);
    }

    @Persistable(name="Minute")
    public int getMinute() {
        return _dateTime.get(Calendar.MINUTE);
    }

    @Persistable(name="Second")
    public int getSecond() {
        return _dateTime.get(Calendar.SECOND);
    }

    @Persistable(name="Hour")
    public int getHour() {
        return _dateTime.get(Calendar.HOUR_OF_DAY);
    }

    @Persistable(name="DayOfWeek")
    public int getDayOfWeek() {
        return _dateTime.get(Calendar.DAY_OF_WEEK);
    }

    public DateTimeInfo() {
        _dateTime = Calendar.getInstance();
    }
}