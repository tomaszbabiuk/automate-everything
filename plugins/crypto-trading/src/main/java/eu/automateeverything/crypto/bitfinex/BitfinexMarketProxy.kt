//package eu.automateeverything.crypto.bitfinex
//
//import eu.automateeverything.crypto.MarketProxy
//import org.knowm.xchange.bitfinex.service.BitfinexAdapters
//import org.knowm.xchange.bitfinex.service.BitfinexMarketDataService
//import org.knowm.xchange.currency.CurrencyPair
//import org.ta4j.core.BaseBar
//import java.time.Duration
//
//class BitfinexMarketProxy(private val bitfinexMarketDataService: BitfinexMarketDataService)
//    : MarketProxy {
//
//    override suspend fun getSymbols(currencyFilter: List<String>): List<CurrencyPair> {
//        return bitfinexMarketDataService.exchangeSymbols
//    }
//
//    override suspend fun getTickers(selectedPairs: List<CurrencyPair>): List<Pair<CurrencyPair, Double>> {
//        return bitfinexMarketDataService
//            .getBitfinexTickers(selectedPairs)
//            .map { val pair = BitfinexAdapters.adaptCurrencyPair(it.symbol)
//                   Pair(pair, it.lastPrice.toDouble())
//            }
//            .toList()
//    }
//
//    override suspend fun getWeeklyData(pair: CurrencyPair, fromInMs: Long, toInMs: Long): List<BaseBar> {
//        return getHistoricCandles("7D", pair, fromInMs, toInMs, Duration.ofDays(7))
//    }
//
//    override suspend fun getDailyData(pair: CurrencyPair, fromInMs: Long, toInMs: Long): List<BaseBar> {
//        return getHistoricCandles("1D", pair, fromInMs, toInMs, Duration.ofDays(1))
//    }
//
//    override suspend fun getHourlyData(pair: CurrencyPair, fromInMs: Long, toInMs: Long): List<BaseBar> {
//        return getHistoricCandles("1h", pair, fromInMs, toInMs, Duration.ofHours(1))
//    }
//
//    private fun getHistoricCandles(candlePeriod: String, pair: CurrencyPair, from: Long, to: Long, barDuration: Duration): List<BaseBar> {
//        return bitfinexMarketDataService
//            .getHistoricCandles(candlePeriod, pair, 201, from, to, 0)
//            .map { BaseBar(barDuration, it.candleDateTime, it.open, it.high, it.low, it.close, it.volume) }
//    }
//}
//
//
