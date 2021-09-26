package eu.automateeverything.crypto

import eu.automateeverything.domain.automation.IValueNode
import eu.automateeverything.domain.hardware.PortValue
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseBarSeries
import org.ta4j.core.indicators.*
import org.ta4j.core.indicators.bollinger.BollingerBandWidthIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator
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
            Indicator.Ema34 -> {
                val closePriceIndicator = ClosePriceIndicator(data)
                EMAIndicator(closePriceIndicator, 34)
            }
            Indicator.Ema55 -> {
                val closePriceIndicator = ClosePriceIndicator(data)
                EMAIndicator(closePriceIndicator, 55)
            }
            Indicator.Ema89 -> {
                val closePriceIndicator = ClosePriceIndicator(data)
                EMAIndicator(closePriceIndicator, 89)
            }
            Indicator.Ema200 -> {
                val closePriceIndicator = ClosePriceIndicator(data)
                EMAIndicator(closePriceIndicator, 200)
            }
            Indicator.Sma50 -> {
                val closePriceIndicator = ClosePriceIndicator(data)
                SMAIndicator(closePriceIndicator, 50)
            }
            Indicator.Sma100 ->  {
                val closePriceIndicator = ClosePriceIndicator(data)
                SMAIndicator(closePriceIndicator, 100)
            }
            Indicator.Sma200 ->  {
                val closePriceIndicator = ClosePriceIndicator(data)
                SMAIndicator(closePriceIndicator, 200)
            }
            Indicator.Roc100 -> {
                val closePriceIndicator = ClosePriceIndicator(data)
                ROCIndicator(closePriceIndicator, 100)
            }
            Indicator.BollingerBandsUpper -> {
                val closePriceIndicator = ClosePriceIndicator(data)
                val ema20Indicator = EMAIndicator(closePriceIndicator, 20)
                val sd20Indicator = StandardDeviationIndicator(closePriceIndicator, 20)
                val middleBandIndicator = BollingerBandsMiddleIndicator(ema20Indicator)
                BollingerBandsUpperIndicator(middleBandIndicator, sd20Indicator)
            }
            Indicator.BollingerBandsMiddle -> {
                val closePriceIndicator = ClosePriceIndicator(data)
                val ema20Indicator = EMAIndicator(closePriceIndicator, 20)
                BollingerBandsMiddleIndicator(ema20Indicator)
            }
            Indicator.BollingerBandsLower -> {
                val closePriceIndicator = ClosePriceIndicator(data)
                val ema20Indicator = EMAIndicator(closePriceIndicator, 20)
                val sd20Indicator = StandardDeviationIndicator(closePriceIndicator, 20)
                val middleBandIndicator = BollingerBandsMiddleIndicator(ema20Indicator)
                BollingerBandsLowerIndicator(middleBandIndicator, sd20Indicator)
            }
            Indicator.BollingerWidth -> {
                val closePriceIndicator = ClosePriceIndicator(data)
                val ema20Indicator = EMAIndicator(closePriceIndicator, 20)
                val sd20Indicator = StandardDeviationIndicator(closePriceIndicator, 20)
                val middleBandIndicator = BollingerBandsMiddleIndicator(ema20Indicator)
                val upBandIndicator = BollingerBandsUpperIndicator(middleBandIndicator, sd20Indicator)
                val lowBandIndicator = BollingerBandsLowerIndicator(middleBandIndicator, sd20Indicator)
                BollingerBandWidthIndicator(upBandIndicator, middleBandIndicator, lowBandIndicator)
            }
        }

        return Ticker(indicator.getValue(indicator.barSeries.barCount - 1).doubleValue())
    }
}
