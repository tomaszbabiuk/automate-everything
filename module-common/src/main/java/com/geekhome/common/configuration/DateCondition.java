package com.geekhome.common.configuration;

public class DateCondition extends ConditionBase {
    private String _startDate;
    private String _stopDate;

    @Persistable(name = "StartDate")
    public String getStartDate() {
        return _startDate;
    }

    public void setStartDate(String value) {
        _startDate = value;
    }

    @Persistable(name = "StopDate")
    public String getStopDate() {
        return _stopDate;
    }

    public void setStopDate(String value) {
        _stopDate = value;
    }

    public DateCondition(DescriptiveName name, String startDate, String stopDate) {
        super(name);
        setStartDate(startDate);
        setStopDate(stopDate);
    }

}
