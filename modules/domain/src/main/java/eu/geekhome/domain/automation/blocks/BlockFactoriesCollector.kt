package eu.geekhome.domain.automation.blocks

import eu.geekhome.domain.automation.BlockFactory
import eu.geekhome.domain.configurable.Configurable
import org.pf4j.ExtensionPoint

interface BlockFactoriesCollector: ExtensionPoint {
    fun collect(thisDevice: Configurable?): List<BlockFactory<*>>
}