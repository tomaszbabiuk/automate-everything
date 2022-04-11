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

package eu.automateeverything.crypto

import eu.automateeverything.data.settings.SettingsDto
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.events.PortUpdateEventData
import eu.automateeverything.domain.hardware.HardwareAdapterBase
import kotlinx.coroutines.*
import org.ta4j.core.BaseBar
import java.util.*

class ExchangeHardwareAdapter(
    override val id: String,
    owningPluginId: String,
    private val marketProxy: MarketProxy,
    eventsSink: EventsSink,
    private val settings: List<SettingsDto>)
    : HardwareAdapterBase<MarketPort>(owningPluginId, id, eventsSink) {

    private var operationScope: CoroutineScope? = null
    private var currencyFilter: List<String>? = null
    private var currencyFilterDefaults = marketPairsStringToList(MarketPairsSettingGroup.FIELD_MARKET_PAIRS_IV)

    companion object {
        const val REFRESH_INTERVAL = 30 * 1000L
        const val REFRESH_OVERLAP = 10 * 1000L
    }

    override fun executePendingChanges() {
        //This adapter is read-only
    }

    override fun stop() {
        operationScope?.cancel("Stop called")
    }

    override fun start() {
        settings
            .filter{it.clazz == MarketPairsSettingGroup::class.java.name}
            .forEach {
                val currencyFilterRaw = it.fields[MarketPairsSettingGroup.FIELD_MARKET_PAIRS]
                currencyFilter = marketPairsStringToList(currencyFilterRaw)
            }

        if (operationScope != null) {
            operationScope!!.cancel("Adapter already started")
        }

        operationScope = CoroutineScope(Dispatchers.IO)
        operationScope?.launch {
            while (isActive) {
                delay(REFRESH_INTERVAL)
                maintenanceLoop()
            }
        }
    }

    override suspend fun internalDiscovery(eventsSink: EventsSink): List<MarketPort> {
        logDiscovery("Connecting to Coingecko")
        val result = ArrayList<MarketPort>()
        val filterRaw = if (currencyFilter == null) {
            currencyFilterDefaults
        } else {
            currencyFilter!!
        }

        val currencyFilter = filterRaw.mapNotNull {
            val baseAndCounter = it.split("/")
            var pair: CurrencyPair? = null
            if (baseAndCounter.size == 2) {
                val base = baseAndCounter[0]
                val counter = baseAndCounter[1]
                pair = CurrencyPair(base, counter)
            }

            pair
        }

        val calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val tickers = marketProxy.getTickers(currencyFilter)
        tickers.forEach {
            logDiscovery("Including market $id ${it.key}")
            val port = MarketPort("$id ${it.key}", it.key, it.value, calendar.timeInMillis)
            updateBars(port, dayOfYear, hourOfDay)
            result.add(port)
        }

        logDiscovery("Finished")
        return result
    }

    private suspend fun maintenanceLoop() {
        val pairs = ports.values.map { it.pair }
        val calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

        try {
            val tickers = marketProxy.getTickers(pairs)
            ports.values.forEach { port ->
                val ticker = tickers[port.pair]!!
                updateBars(port, dayOfYear, hourOfDay)

                port.lastSeenTimestamp = calendar.timeInMillis
                port.updateValue(ticker)
                val event = PortUpdateEventData(owningPluginId, id, port)
                eventsSink.broadcastEvent(event)
            }
        } catch (ignored: Exception) {
            //lastSeen will not be updated when disconnected
        }
    }

    private suspend fun updateBars(port: MarketPort, dayOfYear: Int, hourOfDay: Int) {
        suspend fun feedWeeklyData(pair: CurrencyPair): List<BaseBar> {
            val calendar = Calendar.getInstance()
            val now = calendar.timeInMillis
            calendar.add(Calendar.YEAR, -1)
            val nowMinusYear = calendar.timeInMillis
            return marketProxy.getWeeklyData(pair, nowMinusYear, now)
        }

        suspend fun feedDailyData(pair: CurrencyPair): List<BaseBar> {
            val calendar = Calendar.getInstance()
            val now = calendar.timeInMillis
            calendar.add(Calendar.DAY_OF_YEAR, -201)
            val nowMinus201Days = calendar.timeInMillis
            return marketProxy.getDailyData(pair, nowMinus201Days, now)
        }

        suspend fun feedHourlyData(pair: CurrencyPair): List<BaseBar> {
            val calendar = Calendar.getInstance()
            val now = calendar.timeInMillis
            calendar.add(Calendar.HOUR, -201)
            val nowMinus201Hours = calendar.timeInMillis
            return marketProxy.getHourlyData(pair, nowMinus201Hours, now)
        }

        if (port.lastDailyDataFrom != dayOfYear) {
            port.dailyData = feedDailyData(port.pair)
            port.weeklyData = feedWeeklyData(port.pair)
            port.lastDailyDataFrom = dayOfYear
        }

        if (port.lastHourlyDataFrom != hourOfDay) {
            port.hourlyData = feedHourlyData(port.pair)
            port.lastHourlyDataFrom = hourOfDay
        }
    }

    private fun marketPairsStringToList(raw: String?) : List<String> {
        if (raw == null) {
            return listOf()
        }

        return raw
            .lowercase()
            .replace(";",",")
            .replace(" ", "")
            .split(",")
    }
}