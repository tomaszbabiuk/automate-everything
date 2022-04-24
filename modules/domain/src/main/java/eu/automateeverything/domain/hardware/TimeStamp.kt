/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
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

package eu.automateeverything.domain.hardware

import eu.automateeverything.data.localization.Resource
import eu.automateeverything.data.hardware.PortValue
import java.math.BigDecimal
import java.util.*
import java.util.Calendar.*

class TimeStamp(val value: BigDecimal) : PortValue {
    override fun toFormattedString(): Resource {
        fun addTrailingZero(input: Int, length: Int = 2) : String {
            var result = input.toString()
            while (result.length < length) {
                result = "0$result"
            }
            return result
        }

        val calendar = GregorianCalendar.getInstance()
        calendar.timeInMillis = value.toLong()
        val uniTime = addTrailingZero(calendar.get(HOUR_OF_DAY)-1) + ":" +
                      addTrailingZero(calendar.get(MINUTE)) + ":" +
                      addTrailingZero(calendar.get(SECOND)) + "." +
                      addTrailingZero(calendar.get(MILLISECOND), 3)

        return Resource.createUniResource(uniTime)
    }

    override fun asDecimal(): BigDecimal {
        return value
    }

    companion object {
        fun fromCalendar(now: Calendar): TimeStamp {
            val nowSeconds = now.get(Calendar.HOUR_OF_DAY)*3600 + now.get(Calendar.MINUTE)*60 + now.get(Calendar.SECOND)
            val nowMilliseconds = nowSeconds * 1000 + now.get(Calendar.MILLISECOND)
            return TimeStamp(nowMilliseconds.toBigDecimal())
        }
    }
}