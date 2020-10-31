package com.geekhome.common.configuration;

import com.geekhome.common.configuration.ConditionBase;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;

public class TimeCondition extends ConditionBase {
    private String _startTime;
    private String _stopTime;
    private String _daysIds;

    @Persistable(name="StartTime")
    public String getStartTime() {
        return _startTime;
    }

    public void setStartTime(String value) {
        _startTime = value;
    }

    @Persistable(name="StopTime")
    public String getStopTime() {
        return _stopTime;
    }

    public void setStopTime(String value) {
        _stopTime = value;
    }

    @Persistable(name="DaysIds")
    public String getDaysIds() {
        return _daysIds;
    }

    public void setDaysIds(String value) {
        _daysIds = value;
    }

    public TimeCondition(DescriptiveName name, String startTime, String stopTime, String daysIds) {
        super(name);
        setStartTime(startTime);
        setStopTime(stopTime);
        setDaysIds(daysIds);
    }
}
