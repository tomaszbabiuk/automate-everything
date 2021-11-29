package eu.automateeverything.crypto.coingeckoapi

import eu.automateeverything.crypto.CurrencyPair
import eu.automateeverything.crypto.MarketProxy
import org.ta4j.core.BaseBar
import java.math.BigDecimal
import java.util.*

class CoinGeckoMarketProxy(private val api: CoinGeckoApi) : MarketProxy {

    private var baseCoinsCache: List<Coin>? = null

    private suspend fun baseSymbolToGeckoId(symbol: String) : String? {
        if (baseCoinsCache == null) {
            baseCoinsCache = api.listBaseCoins()
        }

        val coinFound = baseCoinsCache!!
            .find { it.symbol == symbol } ?: return null

        return coinFound.id
    }

    private suspend fun geckoIdToBaseSymbol(geckoId: String) : String? {
        if (baseCoinsCache == null) {
            baseCoinsCache = api.listBaseCoins()
        }

        val coinFound = baseCoinsCache!!
            .find { it.id == geckoId } ?: return null

        return coinFound.symbol
    }

    override suspend fun getTickers(currencyFilter: List<CurrencyPair>): Map<CurrencyPair, BigDecimal> {
        val geckoIdsOfBase =  currencyFilter
            .mapNotNull {
                baseSymbolToGeckoId(it.base.lowercase())
            }

        val counters = currencyFilter
            .map {
                it.counter.lowercase()
            }.distinct()

        return api
            .listTickers(geckoIdsOfBase, counters)
            .map { mainMap ->
                mainMap.value.map {
                    val pair = CurrencyPair(geckoIdToBaseSymbol(mainMap.key)!!, it.key)
                    val value = it.value.toBigDecimal()
                    Pair(pair, value)
                }
            }
            .flatten()
            .toMap()
    }

    override suspend fun getWeeklyData(pair: CurrencyPair, fromTimestamp: Long, toTimestamp: Long): List<BaseBar> {
        return getChartData(pair, fromTimestamp, toTimestamp, ChartRange.Week)
    }

    override suspend fun getDailyData(pair: CurrencyPair, fromTimestamp: Long, toTimestamp: Long): List<BaseBar> {
        return getChartData(pair, fromTimestamp, toTimestamp, ChartRange.Day)
    }

    override suspend fun getHourlyData(pair: CurrencyPair, fromTimestamp: Long, toTimestamp: Long): List<BaseBar> {
        return getChartData(pair, fromTimestamp, toTimestamp, ChartRange.Hour)
    }

    private suspend fun getChartData(pair: CurrencyPair, fromTimestamp: Long, toTimestamp: Long, chartRange: ChartRange): List<BaseBar> {
        println("${pair.base}/${pair.counter}, $chartRange")

        val marketChart201d = api.marketChart(
            baseSymbolToGeckoId(pair.base.lowercase())!!,
            pair.counter.lowercase(),
            fromTimestamp/1000,
            toTimestamp/1000)

        return marketChart201d
            .prices
            .map {
                val calendar = GregorianCalendar()
                calendar.timeInMillis = it.key
                val volume = marketChart201d.total_volumes[it.key]!!
                Triple(calendar, it.value, volume)
            }
            .groupBy { it.first.get(chartRange.calendarField) }
            .map { grouped ->
                val openPrice = grouped.value.first().second
                val closePrice = grouped.value.last().second
                var minPrice = openPrice
                var maxPrice = openPrice
                var totalVol = 0.0
                grouped.value.forEach {
                    if (it.second < minPrice) {
                        minPrice = it.second
                    }

                    if (it.second > maxPrice) {
                        maxPrice = it.second
                    }

                    totalVol += it.third
                }

                val closeTime = grouped.value.last().first.toZonedDateTime()

                println("$closeTime,$openPrice,$closePrice,$minPrice,$maxPrice,$totalVol")
                BaseBar(chartRange.duration, closeTime, openPrice, maxPrice, minPrice, closePrice, totalVol)
            }
    }
}