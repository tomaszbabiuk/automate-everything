package eu.geekhome.coreplugin

import eu.geekhome.services.automation.EvaluableAutomationUnit
import java.util.*

class TwilightConditionAutomationUnit(private val _longitude: Double, private val _latitude: Double) :
    EvaluableAutomationUnit() {
    private var _sunriseUTCHour = 0
    private var _sunriseUTCMinute = 0
    private var _sunsetUTCHour = 0
    private var _sunsetUTCMinute = 0
    private var _dayOfLastCalculation = 0
    private val _timeZoneOffsetInMinutes: Int
    private fun calculateSun(now: Calendar, longitude: Double, latitude: Double) {
        val year = now[Calendar.YEAR]
        val month = now[Calendar.MONTH] + 1
        val day = now[Calendar.DAY_OF_MONTH]
        val n3 = Math.PI / 180
        val e6 = if (month <= 2) month + 12 else month
        val e7 = if (month <= 2) year - 1 else year
        val l5 = year / 100
        val l6 = 2 - l5 + l5 / 4
        val l7 = (365.25 * (e7 + 4716)).toInt() + (30.6001 * (e6 + 1)).toInt() + day + l6 - 1524.5
        val m3 = (l7 - 2451545) / 36525
        val m4 = 280.46646 + 36000.76983 * m3 + 0.0003032 * m3 * m3
        val o3 = 57.29577951
        val m5 = 357.52911 + 35999.05029 * m3 - 0.0001537 * m3 * m3
        val n5 = m5 / 360
        val o5 = (n5 - n5.toInt()) * 360
        val m6 = (1.914602 - 0.004817 * m3 - 0.000014 * m3 * m3) * Math.sin(o5 * n3)
        val m7 = (0.019993 - 0.000101 * m3) * Math.sin(2 * o5 * n3)
        val m8 = 0.000289 * Math.sin(3 * o5 * n3)
        val m9 = m6 + m7 + m8
        val n4 = m4 / 360
        val o4 = (n4 - n4.toInt()) * 360
        val n6 = o4 + m9
        val n7 = 125.04 - 1934.136 * m3
        val n9 = if (n7 < 0) n7 + 360 else n7
        val n10 = n6 - 0.00569 - 0.00478 * Math.sin(n9 * n3)
        val m11 = 23.43930278 - 0.0130042 * m3 - 0.000000163 * m3 * m3
        val n11 = Math.sin(m11 * n3) * Math.sin(n10 * n3)
        val n12 = Math.asin(n11) * 180 / Math.PI
        val n15 = longitude / 15
        val m13 = (7.7 * Math.sin((o4 + 78) * n3) - 9.5 * Math.sin(2 * o4 * n3)) / 60
        val o16 = Math.cos(n12 * n3) * Math.cos(latitude * n3)
        val n16 = -0.01483 - Math.sin(n12 * n3) * Math.sin(latitude * n3)
        val p15 = 2 * (Math.acos(n16 / o16) * o3) / 15
        val sunriseCalc = 12 - n15 + m13 - p15 / 2
        val sunsetCalc = 12 - n15 + m13 + p15 / 2
        _sunriseUTCMinute = ((sunriseCalc - sunriseCalc.toInt()) * 100 * 6 / 10).toInt()
        _sunriseUTCHour = sunriseCalc.toInt()
        _sunsetUTCMinute = ((sunsetCalc - sunsetCalc.toInt()) * 100 * 6 / 10).toInt()
        _sunsetUTCHour = sunsetCalc.toInt()
    }

    override fun doEvaluate(now: Calendar): Boolean {
        val lastCalculatedOn = now[Calendar.DAY_OF_YEAR]
        if (_dayOfLastCalculation != lastCalculatedOn) {
            calculateSun(now, _longitude, _latitude)
            _dayOfLastCalculation = lastCalculatedOn
        }
        val start = _sunsetUTCMinute + _sunsetUTCHour * 60
        val stop = _sunriseUTCMinute + _sunriseUTCHour * 60
        val nowMinute = now[Calendar.MINUTE]
        val nowHour = now[Calendar.HOUR_OF_DAY]
        val nowShifted = nowMinute + nowHour * 60 - _timeZoneOffsetInMinutes
        return !(nowShifted >= stop && nowShifted <= start)
    }

    val timeOffsetInMins: Int
        get() {
            val z = Calendar.getInstance().timeZone
            var offset = z.rawOffset
            if (z.inDaylightTime(Date())) {
                offset = offset + z.dstSavings
            }
            val offsetHrs = offset / 1000 / 60 / 60
            val offsetMins = offset / 1000 / 60 % 60
            return offsetMins + offsetHrs * 60
        }

    init {
        _timeZoneOffsetInMinutes = timeOffsetInMins
    }
}