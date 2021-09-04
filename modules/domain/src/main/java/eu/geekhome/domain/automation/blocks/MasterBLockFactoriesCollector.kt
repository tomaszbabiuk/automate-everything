package eu.geekhome.domain.automation.blocks

import eu.geekhome.data.Repository
import eu.geekhome.data.automation.StateType
import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.automation.BlockFactory
import eu.geekhome.domain.configurable.*
import eu.geekhome.domain.extensibility.PluginsCoordinator
import eu.geekhome.domain.hardware.Humidity
import eu.geekhome.domain.hardware.Temperature
import eu.geekhome.domain.hardware.Wattage

class MasterBlockFactoriesCollector(val pluginsCoordinator: PluginsCoordinator,
                              private val repository: Repository
) : BlockFactoriesCollector {

    override fun collect(thisDevice: Configurable?): List<BlockFactory<*>> {
        val result = ArrayList<BlockFactory<*>>()

        result.addAll(collectStaticBlocks())
        result.addAll(collectConditionBlocks())
        result.addAll(collectSensorBlocks())
        result.addAll(collectChangeStateTriggerBlocks())
        result.addAll(collectStateValuesBlocks())

        if (thisDevice != null) {
            result.addAll(collectChangeStateBlocks(thisDevice))
        }

        result.addAll(collectFromCollectors(thisDevice))

        return result
    }

    private fun collectFromCollectors(thisDevice: Configurable?): Collection<BlockFactory<*>> {
        return pluginsCoordinator
            .blockFactoriesCollectors
            .flatMap { it.collect(thisDevice)}
    }

    private fun collectStaticBlocks(): List<BlockFactory<*>> {
        val staticBlocks =  mutableListOf(
            //logic
            LogicAndBlockFactory(),
            LogicOrBlockFactory(),
            LogicNotBlockFactory(),
            LogicIfElseBlockFactory(),
            LogicDelayBlockFactory(),

            //triggers
            TimeloopTriggerBlockFactory(),
        )

        staticBlocks.addAll(pluginsCoordinator.blockFactories)

        return staticBlocks
    }

    private fun collectConditionBlocks(): List<BlockFactory<*>> {
        val instanceBriefs = repository.getAllInstanceBriefs()
        val allConfigurables = pluginsCoordinator.configurables

        return instanceBriefs
            .filter { briefDto ->
                val configurable = allConfigurables.find { it.javaClass.name == briefDto.clazz }
                configurable is ConditionConfigurable
            }
            .map { briefDto ->
                val id = briefDto.id
                val label = Resource.createUniResource(briefDto.name)
                ConditionBlockFactory(id, label)
            }
            .toList()
    }

    private fun collectSensorBlocks(): List<BlockFactory<*>> {
        val instanceBriefs = repository.getAllInstanceBriefs()
        val allConfigurables = pluginsCoordinator.configurables

        return instanceBriefs
            .map {  briefDto ->
                val configurable = allConfigurables.find { it.javaClass.name == briefDto.clazz }
                Pair(briefDto, configurable)
            }
            .filter {
                it.second is SensorConfigurable<*>
            }
            .map { (briefDto, configurable) ->
                val id = briefDto.id
                val sensorConfigurable = configurable as SensorConfigurable<*>
                val label = Resource.createUniResource(briefDto.name)
                val category = sensorConfigurable.blocksCategory

                SensorBlockFactory(sensorConfigurable.valueClazz, category, id, label)
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

    private fun collectChangeStateTriggerBlocks(): List<BlockFactory<*>> {
        val instanceBriefs = repository.getAllInstanceBriefs()
        val allConfigurables = pluginsCoordinator.configurables

        return instanceBriefs
            .filter { briefDto ->
                val configurable = allConfigurables.find { it.javaClass.name == briefDto.clazz }
                configurable is StateDeviceConfigurable
            }
            .map { briefDto ->
                val instanceId = briefDto.id
                val deviceName = briefDto.name ?: "---"
                val configurableClazz = allConfigurables.find { it.javaClass.name == briefDto.clazz } as StateDeviceConfigurable
                StateChangeTriggerBlockFactory(
                    instanceId,
                    deviceName,
                    configurableClazz.states)
            }
            .toList()
    }

    private fun collectStateValuesBlocks(): List<BlockFactory<*>> {
        val instanceBriefs = repository.getAllInstanceBriefs()
        val allConfigurables = pluginsCoordinator.configurables

        return instanceBriefs
            .filter { briefDto ->
                val configurable = allConfigurables.find { it.javaClass.name == briefDto.clazz }
                configurable is StateDeviceConfigurable
            }
            .map { briefDto ->
                val instanceId = briefDto.id
                val deviceName = briefDto.name ?: "---"
                val configurableClazz = allConfigurables.find { it.javaClass.name == briefDto.clazz } as StateDeviceConfigurable
                StateValueBlockFactory(
                    deviceName,
                    instanceId,
                    configurableClazz.states,
                )
            }
            .toList()
    }
}