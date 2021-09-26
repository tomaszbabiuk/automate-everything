package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.domain.automation.BlockFactory
import eu.automateeverything.domain.configurable.Configurable
import org.pf4j.ExtensionPoint

interface BlockFactoriesCollector: ExtensionPoint {
    fun collect(thisDevice: Configurable?): List<BlockFactory<*>>
}