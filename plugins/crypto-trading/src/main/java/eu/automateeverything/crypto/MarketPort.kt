package eu.automateeverything.crypto

import eu.geekhome.domain.hardware.InputPort
import org.knowm.xchange.currency.CurrencyPair

class MarketPort(
    override val id: String,
    private val currencyPair: CurrencyPair,
    initialValue: Double,
    override var connectionValidUntil: Long) :  InputPort<Ticker> {

    override val valueClazz = Ticker::class.java
    var lastValue = initialValue

    override fun read(): Ticker {
        return Ticker(lastValue, currencyPair.base.currencyCode, currencyPair.counter.currencyCode)
    }

    //TODO:Add function for returning candle data
}