package eu.automateeverything.crypto

import org.ta4j.core.BaseBar

interface MarketProxy {
    suspend fun getTickers(currencyFilter: List<CurrencyPair>): Map<CurrencyPair, Double>
    suspend fun getWeeklyData(pair: CurrencyPair, fromTimestamp: Long, toTimestamp: Long): List<BaseBar>
    suspend fun getDailyData(pair: CurrencyPair, fromTimestamp: Long, toTimestamp: Long): List<BaseBar>
    suspend fun getHourlyData(pair: CurrencyPair, fromTimestamp: Long, toTimestamp: Long): List<BaseBar>
}