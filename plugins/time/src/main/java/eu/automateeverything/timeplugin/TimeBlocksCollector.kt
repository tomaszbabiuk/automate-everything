/*
 * Copyright (c) 2019-2021 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
        EquationBlockFactory(SecondOfDayStamp::class.java, TimeBlockCategories.SecondOfDay),
        EvenSecondBlockFactory()
    )


    private fun collectDayStaticBlocks() = listOf(
        DayOfYearStampValueBlockFactory(),
        TodayBlockFactory(),
        ComparisonBlockFactory(DayOfYearStamp::class.java, TimeBlockCategories.DayOfYear),
        EquationBlockFactory(DayOfYearStamp::class.java, TimeBlockCategories.DayOfYear)
    )
}