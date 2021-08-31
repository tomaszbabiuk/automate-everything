package eu.automateeverything.crypto

import eu.geekhome.domain.automation.BlockFactory
import eu.geekhome.domain.automation.blocks.BlockFactoriesCollector
import eu.geekhome.domain.configurable.Configurable
import eu.geekhome.domain.hardware.PortFinder
import org.pf4j.Extension

@Extension
class CryptoBlocksCollector(private val portFinder: PortFinder) : BlockFactoriesCollector {

    override fun collect(thisDevice: Configurable?): List<BlockFactory<*>> {
        val indicatorBlocks = collectIndicatorBlocks()
        val staticBlocks = collectStaticBlocks()

        return staticBlocks + indicatorBlocks
    }

    private fun collectStaticBlocks() = listOf(
        TickerComparisonBlockFactory(),
        TickerEquationBlockFactory(),
        TickerValueBlockFactory()
    )

    private fun collectIndicatorBlocks(): List<IndicatorValueBlockFactory> {
        val tickerPorts = portFinder.listAllOfInputType(Ticker::class.java)

        return tickerPorts
            .filterIsInstance(MarketPort::class.java)
            .map { IndicatorValueBlockFactory(it.pair, it) }
    }
}

