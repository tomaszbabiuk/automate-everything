package eu.automateeverything.crypto.binance

import eu.automateeverything.crypto.MarketPairsSettingGroup
import eu.automateeverything.crypto.MarketPairsSettingGroup.Companion.FIELD_MARKET_PAIRS_IV
import eu.automateeverything.crypto.MarketPort
import eu.automateeverything.crypto.Ticker
import eu.geekhome.data.settings.SettingsDto
import eu.geekhome.domain.events.EventsSink
import eu.geekhome.domain.hardware.HardwareAdapterBase
import eu.geekhome.domain.hardware.Port
import org.knowm.xchange.Exchange
import org.knowm.xchange.ExchangeFactory
import org.knowm.xchange.bitfinex.BitfinexExchange
import org.knowm.xchange.bitfinex.service.BitfinexAdapters
import org.knowm.xchange.bitfinex.service.BitfinexMarketDataService

class BitfinexCryptoHardwareAdapter : HardwareAdapterBase<Port<Ticker>>() {

    private var currencyFilter: List<String>? = null
    private var currencyFilterDefaults = marketPairsStringToList(FIELD_MARKET_PAIRS_IV)

    override fun executePendingChanges() {
    }

    override fun stop() {
    }

    override fun start(operationSink: EventsSink, settings: List<SettingsDto>) {
        settings
            .filter{it.clazz == MarketPairsSettingGroup::class.java.name}
            .forEach {
                val currencyFilterRaw = it.fields[MarketPairsSettingGroup.FIELD_MARKET_PAIRS]
                currencyFilter = marketPairsStringToList(currencyFilterRaw)
            }
    }

    override suspend fun internalDiscovery(eventsSink: EventsSink): List<Port<Ticker>> {
        val result = ArrayList<Port<Ticker>>()
        val bitfinex: Exchange = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange::class.java)
        val marketDataService: BitfinexMarketDataService = bitfinex.marketDataService as BitfinexMarketDataService
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
        tickers
            .forEach {
                val pair = BitfinexAdapters.adaptCurrencyPair(it.symbol)
                val port = MarketPort("Bitfinex $pair", pair, it.lastPrice.toDouble(), 0L)
                result.add(port)
            }


        println(marketDataService.exchangeSymbols)

        return result
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
