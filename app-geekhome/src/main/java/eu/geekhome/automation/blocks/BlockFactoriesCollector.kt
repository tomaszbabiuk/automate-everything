package eu.geekhome.automation.blocks

import eu.geekhome.automation.BlockFactory
import eu.geekhome.rest.getConfigurables
import eu.geekhome.rest.getRepository
import eu.geekhome.services.automation.StateType
import eu.geekhome.services.configurable.ConditionConfigurable
import eu.geekhome.services.configurable.Configurable
import eu.geekhome.services.configurable.StateDeviceConfigurable
import eu.geekhome.services.localization.Resource
import org.pf4j.PluginManager

interface IBlockFactoriesCollector {
    fun collect(thisDevice: Configurable?): List<BlockFactory<*>>
}

class BlockFactoriesCollector(private val pluginManager: PluginManager) : IBlockFactoriesCollector {

   override fun collect(thisDevice: Configurable?): List<BlockFactory<*>> {
        val result = ArrayList<BlockFactory<*>>()

        result.addAll(collectStaticBlocks())
        result.addAll(collectConditionBlocks())

        if (thisDevice != null) {
            result.addAll(collectChangeStateBlocks(thisDevice))
        }

        return result
    }

    private fun collectStaticBlocks(): List<BlockFactory<*>> {
        return listOf(
            LogicAndBlockFactory(),
            LogicOrBlockFactory(),
            LogicNotBlockFactory(),
            LogicIfElseBlockFactory(),
            TimeloopTriggerBlockFactory(),
            TemperatureComparisonBlockFactory(),
            TemperatureEquationBlockFactory(),
            TemperatureValueInCBlockFactory(),
        )
    }

    private fun collectConditionBlocks(): List<BlockFactory<*>> {
        val instanceBriefs = pluginManager.getRepository().getAllInstanceBriefs()
        val allConfigurables = pluginManager.getConfigurables()

        return instanceBriefs
            .filter { briefDto ->
                val configurable = allConfigurables.find { it.javaClass.name == briefDto.clazz }
                configurable is ConditionConfigurable
            }
            .map { briefDto ->
                val conditionId = briefDto.id
                val label = Resource.createUniResource(briefDto.name)
                ConditionBlockFactory(conditionId, label)
            }
            .toList()
    }

    private fun collectChangeStateBlocks(thisDevice: Configurable): List<BlockFactory<*>> {
        if (thisDevice is StateDeviceConfigurable) {
            return thisDevice
                .states
                .filter { it.value.type != StateType.ReadOnly }
                .map {
                    ChangeStateBlockFactory(it.value)
                }
                .toList()
        }

        return listOf()
    }
}