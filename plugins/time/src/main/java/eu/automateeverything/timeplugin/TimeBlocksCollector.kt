package eu.automateeverything.timeplugin

import eu.automateeverything.domain.automation.BlockFactory
import eu.automateeverything.domain.automation.blocks.BlockFactoriesCollector
import eu.automateeverything.domain.configurable.Configurable
import org.pf4j.Extension

@Extension
class TimeBlocksCollector() : BlockFactoriesCollector {

    override fun collect(thisDevice: Configurable?): List<BlockFactory<*>> {
        return collectTimeStaticBlocks() + collectDateStaticBlocks()
    }

    private fun collectTimeStaticBlocks() = listOf(
        TimestampValueBlockFactory(),
        TimeComparisonBlockFactory(),
        TimeEquationBlockFactory(),
    )

    private fun collectDateStaticBlocks() = listOf(
        DateComparisonBlockFactory(),
        DateEquationBlockFactory(),
    )

}