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

package eu.automateeverything.crypto

import eu.automateeverything.crypto.ExchangeHardwareAdapter.Companion.REFRESH_INTERVAL
import eu.automateeverything.crypto.ExchangeHardwareAdapter.Companion.REFRESH_OVERLAP
import eu.automateeverything.domain.hardware.InputPort
import org.ta4j.core.BaseBar
import java.math.BigDecimal

class MarketPort(
    override val id: String,
    val pair: CurrencyPair,
    initialValue: BigDecimal,
    override var lastSeenTimestamp: Long) :  InputPort<Ticker>
{
    override val valueClazz = Ticker::class.java

    override val sleepInterval = REFRESH_INTERVAL + REFRESH_OVERLAP

    var dailyData: List<BaseBar>? = null
    var weeklyData: List<BaseBar>? = null
    var hourlyData: List<BaseBar>? = null
    var lastValue = initialValue
    var lastDailyDataFrom: Int? = null
    var lastHourlyDataFrom: Int? = null

    override fun read(): Ticker {
        return Ticker(lastValue)
    }

    fun updateValue(newValue: BigDecimal) {
        lastValue = newValue
    }
}