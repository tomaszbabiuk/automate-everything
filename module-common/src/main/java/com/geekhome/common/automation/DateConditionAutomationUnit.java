package com.geekhome.common.automation;

import com.geekhome.common.configuration.DateCondition;
import com.geekhome.common.localization.ILocalizationProvider;

import java.util.Calendar;
import java.util.HashMap;

public class DateConditionAutomationUnit extends EvaluableAutomationUnit {
    private final HashMap<String, Integer> _monthNames;
    private final int _startDate;
    private final int _stopDate;

    public DateConditionAutomationUnit(DateCondition condition, ILocalizationProvider localizationProvider) {
        super(condition.getName());
        _monthNames = new HashMap<>();
        _monthNames.put(localizationProvider.getValue("C:January"), 0);
        _monthNames.put(localizationProvider.getValue("C:February"), 1);
        _monthNames.put(localizationProvider.getValue("C:March"), 2);
        _monthNames.put(localizationProvider.getValue("C:April"), 3);
        _monthNames.put(localizationProvider.getValue("C:May"), 4);
        _monthNames.put(localizationProvider.getValue("C:June"), 5);
        _monthNames.put(localizationProvider.getValue("C:July"), 6);
        _monthNames.put(localizationProvider.getValue("C:August"), 7);
        _monthNames.put(localizationProvider.getValue("C:September"), 8);
        _monthNames.put(localizationProvider.getValue("C:October"), 9);
        _monthNames.put(localizationProvider.getValue("C:November"), 10);
        _monthNames.put(localizationProvider.getValue("C:December"), 11);
        _startDate = parseDate(condition.getStartDate());
        _stopDate = parseDate(condition.getStopDate());
    }

    @Override
    protected boolean doEvaluate(Calendar now) {
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        long nowAsNumber = dateToNumber(month, day);
        if (_startDate < _stopDate) {
            return nowAsNumber >= _startDate && (nowAsNumber <= _stopDate);
        }

        return !(nowAsNumber >= _stopDate && (nowAsNumber <= _startDate));
    }

    private int parseDate(String date) {
        String[] dateSplit = date.split(" ");
        int month = _monthNames.get(dateSplit[1]);
        int day = Integer.parseInt(dateSplit[0]);

        return dateToNumber(month, day);
    }

    private static int dateToNumber(int month, int day) {
        return day + month * 31;
    }
}

