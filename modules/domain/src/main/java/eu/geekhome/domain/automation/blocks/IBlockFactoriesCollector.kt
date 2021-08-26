package eu.geekhome.domain.automation.blocks

import eu.geekhome.domain.automation.BlockFactory
import eu.geekhome.domain.configurable.Configurable

interface IBlockFactoriesCollector {
    fun collect(thisDevice: Configurable?): List<BlockFactory<*>>
}