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
    private val owningPluginId: String,
    private val marketProxy: MarketProxy,
    private val eventsSink: EventsSink) : HardwareAdapterBase<MarketPort>() {

    private var operationScope: CoroutineScope? = null
    private var currencyFilter: List<String>? = null
    private var currencyFilterDefaults = marketPairsStringToList(MarketPairsSettingGroup.FIELD_MARKET_PAIRS_IV)
    private val refreshIntervalMs = 30 * 1000L
    private val refreshOverlapMs = 10 * 1000L

    override fun executePendingChanges() {
        //This adapter is read-only
    }

    override fun stop() {
        operationScope?.cancel("Stop called")
    }

    override fun start(settings: List<SettingsDto>) {
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
                delay(refreshIntervalMs)
                maintenanceLoop()
            }
        }
    }

    override suspend fun internalDiscovery(eventsSink: EventsSink): List<MarketPort> {
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
        val validUntil = calculateValidUntil(calendar.timeInMillis)
        val tickers = marketProxy.getTickers(currencyFilter)
        tickers.forEach {
            val port = MarketPort("$id ${it.key}", it.key, it.value, validUntil)
            updateBars(port, dayOfYear, hourOfDay)
            result.add(port)
        }

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

                val prevValue = port.lastValue
                val valueHasChanged = prevValue != ticker
                val wasDisconnected = port.checkIfConnected(calendar)
                if (valueHasChanged || wasDisconnected) {
                    port.updateValue(ticker)
                    val event = PortUpdateEventData(owningPluginId, id, port)
                    eventsSink.broadcastEvent(event)
                }

                port.connectionValidUntil = calculateValidUntil(calendar.timeInMillis)
            }
        } catch (ex: Exception) {
            ports.values.forEach {
                val wasConnected = it.checkIfConnected(calendar)
                it.markDisconnected()
                if (wasConnected) {
                    val event = PortUpdateEventData(owningPluginId, id, it)
                    eventsSink.broadcastEvent(event)
                }
            }
        }
    }

    private fun calculateValidUntil(timeInMillis: Long) : Long {
        return timeInMillis + refreshIntervalMs + refreshOverlapMs
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