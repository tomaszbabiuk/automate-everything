package eu.automateeverything.crypto

import org.ta4j.core.BaseBar
import java.math.BigDecimal

interface MarketProxy {
    suspend fun getTickers(currencyFilter: List<CurrencyPair>): Map<CurrencyPair, BigDecimal>
    suspend fun getWeeklyData(pair: CurrencyPair, fromTimestamp: Long, toTimestamp: Long): List<BaseBar>
    suspend fun getDailyData(pair: CurrencyPair, fromTimestamp: Long, toTimestamp: Long): List<BaseBar>
    suspend fun getHourlyData(pair: CurrencyPair, fromTimestamp: Long, toTimestamp: Long): List<BaseBar>
}