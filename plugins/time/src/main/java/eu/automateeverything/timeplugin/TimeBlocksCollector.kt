package eu.automateeverything.timeplugin

import eu.automateeverything.domain.automation.BlockFactory
import eu.automateeverything.domain.automation.blocks.BlockFactoriesCollector
import eu.automateeverything.domain.automation.blocks.ComparisonBlockFactory
import eu.automateeverything.domain.automation.blocks.EquationBlockFactory
import eu.automateeverything.domain.configurable.Configurable
import org.pf4j.Extension

@Extension
class TimeBlocksCollector() : BlockFactoriesCollector {

    override fun collect(thisDevice: Configurable?): List<BlockFactory<*>> {
        return collectTimeStaticBlocks() + collectDayStaticBlocks()
    }

    private fun collectTimeStaticBlocks() = listOf(
        SecondOfDayValueBlockFactory(),
        NowBlockFactory(),
        ComparisonBlockFactory(SecondOfDayStamp::class.java, TimeBlockCategories.SecondOfDay),
        EquationBlockFactory(SecondOfDayStamp::class.java, TimeBlockCategories.SecondOfDay)
    )


    private fun collectDayStaticBlocks() = listOf(
        DayOfYearStampValueBlockFactory(),
        TodayBlockFactory(),
        ComparisonBlockFactory(DayOfYearStamp::class.java, TimeBlockCategories.DayOfYear),
        EquationBlockFactory(DayOfYearStamp::class.java, TimeBlockCategories.DayOfYear)
    )
}