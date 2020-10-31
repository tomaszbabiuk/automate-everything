package com.geekhome.common.automation;

import com.geekhome.common.configuration.TimeCondition;

import java.util.Calendar;

public class TimeConditionAutomationUnit extends EvaluableAutomationUnit {
    private TimeCondition _condition;

    public TimeConditionAutomationUnit(TimeCondition condition) {
        super(condition.getName());
        _condition = condition;
    }

    @Override
    protected boolean doEvaluate(Calendar now) {
        int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
        boolean found = false;
        for (String dayId : _condition.getDaysIds().split(",")) {
            if (((dayId.equals("mo")) && (dayOfWeek == Calendar.MONDAY)) ||
                    ((dayId.equals("tu")) && (dayOfWeek == Calendar.TUESDAY)) ||
                    ((dayId.equals("we")) && (dayOfWeek == Calendar.WEDNESDAY)) ||
                    ((dayId.equals("th")) && (dayOfWeek == Calendar.THURSDAY)) ||
                    ((dayId.equals("fr")) && (dayOfWeek == Calendar.FRIDAY)) ||
                    ((dayId.equals("sa")) && (dayOfWeek == Calendar.SATURDAY)) ||
                    ((dayId.equals("su")) && (dayOfWeek == Calendar.SUNDAY))) {
                found = true;
            }
        }

        if (!found) {
            return false;
        }

        long start = ParseTime(_condition.getStartTime());
        long stop = ParseTime(_condition.getStopTime());
        long timeOfDay = getTimeOfDay(now);
        if (start < stop) {
            return timeOfDay >= start && (timeOfDay <= stop);
        }

        return !(timeOfDay >= stop && (timeOfDay <= start));
    }

    private long getTimeOfDay(Calendar now) {
        int hours = now.get(Calendar.HOUR_OF_DAY);
        int mins = now.get(Calendar.MINUTE);
        int secsTotal = (mins * 60 + hours * 3600);
        return secsTotal * 1000;
    }

    private static long ParseTime(String time) {
        String[] timeSplitted = time.split(":");
        int hours = Integer.parseInt(timeSplitted[0]);
        int mins = Integer.parseInt(timeSplitted[1]);
        int secsTotal = (mins * 60 + hours * 3600);
        return secsTotal * 1000;
    }
}

