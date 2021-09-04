package eu.automateeverything.crypto

import org.knowm.xchange.currency.CurrencyPair
import org.ta4j.core.BaseBar

interface MarketProxy {
    suspend fun getTickers(currencyFilter: List<CurrencyPair>): List<Pair<CurrencyPair, Double>>
    suspend fun getWeeklyData(pair: CurrencyPair, fromTimestamp: Long, toTimestamp: Long): List<BaseBar>
    suspend fun getDailyData(pair: CurrencyPair, fromTimestamp: Long, toTimestamp: Long): List<BaseBar>
    suspend fun getHourlyData(pair: CurrencyPair, fromTimestamp: Long, toTimestamp: Long): List<BaseBar>
}