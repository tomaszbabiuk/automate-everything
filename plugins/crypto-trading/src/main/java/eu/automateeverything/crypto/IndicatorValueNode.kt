package eu.automateeverything.crypto

import eu.geekhome.domain.automation.IValueNode
import eu.geekhome.domain.hardware.InputPort
import eu.geekhome.domain.hardware.PortValue
import java.util.*

class IndicatorValueNode(
    private val tickerPort: InputPort<Ticker>,
    private val indicator: Indicator,
    private val interval: Interval
) : IValueNode {

    override fun getValue(now: Calendar): PortValue {
//        val series = indicatorsData.series
//        val closePriceIndicator = ClosePriceIndicator(series)
//        val rsiIndicator = RSIIndicator(closePriceIndicator, 14)
        println(indicator)
        println(interval)
        return tickerPort.read()
    }
}
