package eu.automateeverything.crypto

import eu.geekhome.domain.automation.IValueNode
import eu.geekhome.domain.hardware.PortValue
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseBarSeries
import org.ta4j.core.indicators.CachedIndicator
import org.ta4j.core.indicators.EMAIndicator
import org.ta4j.core.indicators.RSIIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.num.Num
import java.util.*

class IndicatorValueNode(
    private val tickerPort: MarketPort,
    private val indicator: Indicator,
    private val interval: Interval
) : IValueNode {

    override fun getValue(now: Calendar): PortValue? {
        val dataSource = when (interval) {
            Interval.Day -> tickerPort.dailyData
            Interval.Hour -> tickerPort.hourlyData
            Interval.Week -> tickerPort.weeklyData
        } ?: return null

        val data: BarSeries = BaseBarSeries("${interval.label} series", dataSource.sortedBy { it.endTime })

        val indicator: CachedIndicator<Num> = when(indicator) {
            Indicator.RSI14 -> {
                val closePriceIndicator = ClosePriceIndicator(data)
                RSIIndicator(closePriceIndicator, 14)
            }
            Indicator.Ema21 -> {
                val closePriceIndicator = ClosePriceIndicator(data)
                EMAIndicator(closePriceIndicator, 21)
            }
        }

        return Ticker(indicator.getValue(indicator.barSeries.barCount - 1).doubleValue())
    }
}
