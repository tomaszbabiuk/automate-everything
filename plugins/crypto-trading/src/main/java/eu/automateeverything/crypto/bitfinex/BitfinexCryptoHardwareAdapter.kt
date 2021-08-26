package eu.automateeverything.crypto.bitfinex

import eu.automateeverything.crypto.CryptoTradingPlugin.Companion.PLUGIN_ID
import eu.automateeverything.crypto.MarketPairsSettingGroup
import eu.automateeverything.crypto.MarketPairsSettingGroup.Companion.FIELD_MARKET_PAIRS_IV
import eu.automateeverything.crypto.MarketPort
import eu.geekhome.data.settings.SettingsDto
import eu.geekhome.domain.events.EventsSink
import eu.geekhome.domain.events.PortUpdateEventData
import eu.geekhome.domain.hardware.HardwareAdapterBase
import kotlinx.coroutines.*
import org.knowm.xchange.Exchange
import org.knowm.xchange.ExchangeFactory
import org.knowm.xchange.bitfinex.BitfinexExchange
import org.knowm.xchange.bitfinex.service.BitfinexAdapters
import org.knowm.xchange.bitfinex.service.BitfinexMarketDataService
import kotlin.collections.ArrayList

class BitfinexCryptoHardwareAdapter() : HardwareAdapterBase<MarketPort>() {

    private var operationScope: CoroutineScope? = null
    private var operationSink: EventsSink? = null
    private var currencyFilter: List<String>? = null
    private var currencyFilterDefaults = marketPairsStringToList(FIELD_MARKET_PAIRS_IV)
    private val bitfinex: Exchange = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange::class.java)
    private val marketDataService: BitfinexMarketDataService = bitfinex.marketDataService as BitfinexMarketDataService

    override fun executePendingChanges() {
        //This adapter is read-only
    }

    override fun stop() {
        operationScope?.cancel("Stop called")
    }

    override fun start(operationSink: EventsSink, settings: List<SettingsDto>) {
        this.operationSink = operationSink

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
                maintenanceLoop()
                delay(30000)
            }
        }
    }

    override suspend fun internalDiscovery(eventsSink: EventsSink): List<MarketPort> {
        val result = ArrayList<MarketPort>()
        val filter = if (currencyFilter == null) {
            currencyFilterDefaults
        } else {
            currencyFilter!!
        }

        val selectedPairs = marketDataService
            .exchangeSymbols
            .filter{
                val matchedMarket = "${it.base.currencyCode.lowercase()}/${it.counter.currencyCode.lowercase()}"
                filter.contains(matchedMarket)
            }

        val tickers = marketDataService.getBitfinexTickers(selectedPairs)
        tickers.forEach {
            val pair = BitfinexAdapters.adaptCurrencyPair(it.symbol)
            val port = MarketPort("Bitfinex $pair", pair, it.lastPrice.toDouble(), 0L)
            result.add(port)
        }

        return result
    }

    private suspend fun maintenanceLoop() {
        val pairs = ports.values.map { it.pair }
        val tickers = marketDataService.getBitfinexTickers(pairs)
        tickers.forEach { ticker ->
            val pair = BitfinexAdapters.adaptCurrencyPair(ticker.symbol)
            ports
                .values
                .filter { port -> port.pair == pair }
                .forEach { port ->
                    val prevValue = port.lastValue
                    val newValue = ticker.lastPrice.toDouble()
                    val valueHasChanged = prevValue != newValue
                    if (valueHasChanged) {
                        port.updateValue(newValue)
                        val event = PortUpdateEventData(PLUGIN_ID, ADAPTER_ID, port)
                        operationSink?.broadcastEvent(event)
                    }
                }
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

    override val id: String
        get() = TODO("Not yet implemented")

    companion object {
        const val ADAPTER_ID = "bitfinex"
    }
}
