package eu.geekhome.coreplugin;


import eu.geekhome.services.automation.EvaluableAutomationUnit;
import eu.geekhome.services.configurable.Configurable;
import eu.geekhome.services.configurable.FieldDefinition;
import eu.geekhome.services.repository.InstanceDto;

import java.util.Calendar;
import java.util.TimeZone;

public class TwilightConditionAutomationUnit extends EvaluableAutomationUnit {
    private int _sunriseUTCHour;
    private int _sunriseUTCMinute;
    private int _sunsetUTCHour;
    private int _sunsetUTCMinute;
    private int _dayOfLastCalculation;
    private final int _timeZoneOffsetInMinutes;
    private double _longitude;
    private double _latitude;

    public TwilightConditionAutomationUnit(double longitude, double latitude) {
        _longitude = longitude;
        _latitude = latitude;
        _timeZoneOffsetInMinutes =  getTimeOffsetInMins();
    }

    private void calculateSun(Calendar now, double longitude, double latitude) {
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);

        double n3 = Math.PI / 180;
        int e6 = (month <= 2) ? month + 12 : month;
        int e7 = (month <= 2) ? year - 1 : year;

        int l5 = (year / 100);
        int l6 = 2 - l5 + (l5 / 4);
        double l7 = (int)(365.25 * (e7 + 4716)) + (int)(30.6001 * (e6 + 1)) + day + l6 - 1524.5;
        double m3 = (l7 - 2451545) / 36525;
        double m4 = 280.46646 + 36000.76983 * m3 + 0.0003032 * m3 * m3;
        double o3 = 57.29577951;
        double m5 = 357.52911 + 35999.05029 * m3 - 0.0001537 * m3 * m3;
        double n5 = m5 / 360;
        double o5 = (n5 - (int)(n5)) * 360;
        double m6 = (1.914602 - 0.004817 * m3 - 0.000014 * m3 * m3) * Math.sin(o5 * n3);
        double m7 = (0.019993 - 0.000101 * m3) * Math.sin(2 * o5 * n3);
        double m8 = 0.000289 * Math.sin(3 * o5 * n3);
        double m9 = m6 + m7 + m8;
        double n4 = m4 / 360;
        double o4 = (n4 - (int)(n4)) * 360;
        double n6 = o4 + m9;
        double n7 = 125.04 - 1934.136 * m3;
        double n9 = (n7 < 0) ? n7 + 360 : n7;
        double n10 = n6 - 0.00569 - 0.00478 * Math.sin(n9 * n3);
        double m11 = 23.43930278 - 0.0130042 * m3 - 0.000000163 * m3 * m3;
        double n11 = Math.sin(m11 * n3) * Math.sin(n10 * n3);
        double n12 = Math.asin(n11) * 180 / Math.PI;
        double n15 = longitude / 15;
        double m13 = (7.7 * Math.sin((o4 + 78) * n3) - 9.5 * Math.sin(2 * o4 * n3)) / 60;
        double o16 = Math.cos(n12 * n3) * Math.cos(latitude * n3);
        double n16 = -0.01483 - Math.sin(n12 * n3) * Math.sin(latitude * n3);
        double p15 = 2 * (Math.acos(n16 / o16) * o3) / 15;
        double sunriseCalc = 12 - n15 + m13 - (p15 / 2);
        double sunsetCalc = 12 - n15 + m13 + (p15 / 2);

        _sunriseUTCMinute = (int) ((sunriseCalc - (int) (sunriseCalc)) * 100 * 6 / 10);
        _sunriseUTCHour = (int) sunriseCalc;

        _sunsetUTCMinute = (int) ((sunsetCalc - (int) (sunsetCalc)) * 100 * 6 / 10);
        _sunsetUTCHour = (int) sunsetCalc;
    }


    @SuppressWarnings("unchecked")
    @Override
    protected boolean doEvaluate(Calendar now) {
        int lastCalculatedOn = now.get(Calendar.DAY_OF_YEAR);
        if (_dayOfLastCalculation != lastCalculatedOn) {
            calculateSun(now, _longitude, _latitude);
            _dayOfLastCalculation = lastCalculatedOn;
        }

        int start = _sunsetUTCMinute + _sunsetUTCHour*60;
        int stop = _sunriseUTCMinute + _sunriseUTCHour*60;

        int nowMinute = now.get(Calendar.MINUTE);
        int nowHour = now.get(Calendar.HOUR_OF_DAY);

        int nowShifted = nowMinute + nowHour *60 - _timeZoneOffsetInMinutes;

        return !(nowShifted >= stop && (nowShifted <= start));
    }


    public int getTimeOffsetInMins() {
        TimeZone z = Calendar.getInstance().getTimeZone();
        int offset = z.getRawOffset();
        if(z.inDaylightTime(new java.util.Date())){
            offset = offset + z.getDSTSavings();
        }
        int offsetHrs = offset / 1000 / 60 / 60;
        int offsetMins = offset / 1000 / 60 % 60;
        return offsetMins + offsetHrs*60;
    }
}