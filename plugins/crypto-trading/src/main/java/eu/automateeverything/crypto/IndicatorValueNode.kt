package eu.automateeverything.crypto

import eu.geekhome.domain.automation.IValueNode
import eu.geekhome.domain.hardware.InputPort
import eu.geekhome.domain.hardware.PortValue
import java.util.*

class IndicatorValueNode(val tickerPort: InputPort<Ticker>, val indicator: Unit, val interval: Unit) : IValueNode {
    override fun getValue(now: Calendar): PortValue? {
        TODO("Not yet implemented")
//        val series = indicatorsData.series
//        val closePriceIndicator = ClosePriceIndicator(series)
//        val rsiIndicator = RSIIndicator(closePriceIndicator, 14)
    }
}
