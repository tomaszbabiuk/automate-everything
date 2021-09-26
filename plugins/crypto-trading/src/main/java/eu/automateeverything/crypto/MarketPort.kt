package eu.automateeverything.crypto

import eu.automateeverything.domain.hardware.InputPort
import org.ta4j.core.BaseBar

class MarketPort(
    override val id: String,
    val pair: CurrencyPair,
    initialValue: Double,
    override var connectionValidUntil: Long) :  InputPort<Ticker>
{

    override val valueClazz = Ticker::class.java

    var dailyData: List<BaseBar>? = null
    var weeklyData: List<BaseBar>? = null
    var hourlyData: List<BaseBar>? = null
    var lastValue = initialValue
    var lastDailyDataFrom: Int? = null
    var lastHourlyDataFrom: Int? = null

    override fun read(): Ticker {
        return Ticker(lastValue)
    }

    fun updateValue(newValue: Double) {
        lastValue = newValue
    }
}