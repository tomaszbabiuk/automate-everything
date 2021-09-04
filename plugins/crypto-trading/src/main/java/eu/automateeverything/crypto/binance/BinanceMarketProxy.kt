//package eu.automateeverything.crypto.binance
//
//import eu.automateeverything.crypto.MarketProxy
//import org.knowm.xchange.binance.dto.marketdata.KlineInterval
//import org.knowm.xchange.binance.service.BinanceMarketDataService
//import org.knowm.xchange.currency.CurrencyPair
//import org.ta4j.core.BaseBar
//import java.time.Duration
//import java.time.Instant
//import java.time.ZoneOffset
//import java.time.ZonedDateTime
//
//class BinanceMarketProxy(private val binanceMarketDataService: BinanceMarketDataService)
//    : MarketProxy {
//
//    override suspend fun getSymbols(currencyFilter: List<String>): List<CurrencyPair> {
//        return binanceMarketDataService
//                    .exchangeInfo
//                    .symbols
//                    .map { CurrencyPair(it.baseAsset, it.quoteAsset)}
//                    .toList()
//    }
//
//    override suspend fun getTickers(selectedPairs: List<CurrencyPair>): List<Pair<CurrencyPair, Double>> {
//        return binanceMarketDataService.tickerAllPrices()
//            .filter { selectedPairs.contains(it.currencyPair) }
//            .map { Pair(it.currencyPair, it.price.toDouble()) }
//            .toList()
//    }
//
//    override suspend fun getWeeklyData(pair: CurrencyPair, fromInMs: Long, toInMs: Long): List<BaseBar> {
//        return getHistoricCandles(KlineInterval.w1, pair, fromInMs, toInMs, Duration.ofDays(7))
//    }
//
//    override suspend fun getDailyData(pair: CurrencyPair, fromInMs: Long, toInMs: Long): List<BaseBar> {
//        return getHistoricCandles(KlineInterval.d1, pair, fromInMs, toInMs, Duration.ofDays(1))
//    }
//
//    override suspend fun getHourlyData(pair: CurrencyPair, fromInMs: Long, toInMs: Long): List<BaseBar> {
//        return getHistoricCandles(KlineInterval.h1, pair, fromInMs, toInMs, Duration.ofHours(1))
//    }
//
//    private fun getHistoricCandles(interval: KlineInterval, pair: CurrencyPair, from: Long, to: Long, barDuration: Duration): List<BaseBar> {
//        return binanceMarketDataService
//            .klines(pair, interval, 201, from, to)
//            .map {
//                val instant = Instant.ofEpochMilli(it.closeTime)
//                val closeTime = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC)
//                BaseBar(barDuration, closeTime, it.openPrice, it.highPrice, it.lowPrice, it.closePrice, it.volume)
//            }
//    }
//}
//
//
