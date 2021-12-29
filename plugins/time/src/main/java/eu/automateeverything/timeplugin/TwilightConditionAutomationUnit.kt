/*
 * Copyright (c) 2019-2021 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything.timeplugin

import eu.automateeverything.domain.automation.EvaluableAutomationUnitBase
import java.util.*
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin

class TwilightConditionAutomationUnit(private val longitude: Double, private val latitude: Double) :
    EvaluableAutomationUnitBase() {

    private var sunriseUTCHour = 0
    private var sunriseUTCMinute = 0
    private var sunsetUTCHour = 0
    private var sunsetUTCMinute = 0
    private var dayOfLastCalculation = 0
    private val timeZoneOffsetInMinutes: Int

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
        val m6 = (1.914602 - 0.004817 * m3 - 0.000014 * m3 * m3) * sin(o5 * n3)
        val m7 = (0.019993 - 0.000101 * m3) * sin(2 * o5 * n3)
        val m8 = 0.000289 * sin(3 * o5 * n3)
        val m9 = m6 + m7 + m8
        val n4 = m4 / 360
        val o4 = (n4 - n4.toInt()) * 360
        val n6 = o4 + m9
        val n7 = 125.04 - 1934.136 * m3
        val n9 = if (n7 < 0) n7 + 360 else n7
        val n10 = n6 - 0.00569 - 0.00478 * sin(n9 * n3)
        val m11 = 23.43930278 - 0.0130042 * m3 - 0.000000163 * m3 * m3
        val n11 = sin(m11 * n3) * sin(n10 * n3)
        val n12 = asin(n11) * 180 / Math.PI
        val n15 = longitude / 15
        val m13 = (7.7 * sin((o4 + 78) * n3) - 9.5 * sin(2 * o4 * n3)) / 60
        val o16 = cos(n12 * n3) * cos(latitude * n3)
        val n16 = -0.01483 - sin(n12 * n3) * sin(latitude * n3)
        val p15 = 2 * (acos(n16 / o16) * o3) / 15
        val sunriseCalc = 12 - n15 + m13 - p15 / 2
        val sunsetCalc = 12 - n15 + m13 + p15 / 2
        sunriseUTCMinute = ((sunriseCalc - sunriseCalc.toInt()) * 100 * 6 / 10).toInt()
        sunriseUTCHour = sunriseCalc.toInt()
        sunsetUTCMinute = ((sunsetCalc - sunsetCalc.toInt()) * 100 * 6 / 10).toInt()
        sunsetUTCHour = sunsetCalc.toInt()
    }

    override fun doEvaluate(now: Calendar): Boolean {
        val lastCalculatedOn = now[Calendar.DAY_OF_YEAR]
        if (dayOfLastCalculation != lastCalculatedOn) {
            calculateSun(now, longitude, latitude)
            dayOfLastCalculation = lastCalculatedOn
        }
        val start = sunsetUTCMinute + sunsetUTCHour * 60
        val stop = sunriseUTCMinute + sunriseUTCHour * 60
        val nowMinute = now[Calendar.MINUTE]
        val nowHour = now[Calendar.HOUR_OF_DAY]
        val nowShifted = nowMinute + nowHour * 60 - timeZoneOffsetInMinutes
        return nowShifted !in stop..start
    }

    private val timeOffsetInMins: Int
        get() {
            val z = Calendar.getInstance().timeZone
            var offset = z.rawOffset
            if (z.inDaylightTime(Date())) {
                offset += z.dstSavings
            }
            val offsetHrs = offset / 1000 / 60 / 60
            val offsetMins = offset / 1000 / 60 % 60
            return offsetMins + offsetHrs * 60
        }

    init {
        timeZoneOffsetInMinutes = timeOffsetInMins
    }
}