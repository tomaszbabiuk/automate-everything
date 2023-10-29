/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
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

package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.data.Repository
import eu.automateeverything.data.automation.StateType
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.BlockFactory
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.domain.extensibility.PluginsCoordinator

class MasterBlockFactoriesCollector(
    val pluginsCoordinator: PluginsCoordinator,
    private val repository: Repository
) : BlockFactoriesCollector {

    override fun collect(
        thisDevice: Configurable,
        context: CollectionContext
    ): List<BlockFactory<*>> {
        val result = ArrayList<BlockFactory<*>>()

        if (context == CollectionContext.Automation) {
            result.addAll(collectStaticBlocks())
            result.addAll(collectConditionBlocks())
            result.addAll(collectSensorBlocks())
            result.addAll(collectChangeStateTriggerBlocks())

            val portTriggers = collectPortUpdateTriggerBlock()
            if (portTriggers != null) {
                result.add(portTriggers)
            }
            result.addAll(collectStateValuesBlocks())
            result.addAll(collectChangeStateBlocks(thisDevice))
            result.addAll(collectChangeValueBlocks(thisDevice))
        }

        result.addAll(collectFromCollectors(thisDevice, context))

        return result
    }

    private fun collectFromCollectors(
        thisDevice: Configurable,
        context: CollectionContext
    ): Collection<BlockFactory<*>> {
        return pluginsCoordinator.blockFactoriesCollectors.flatMap {
            it.collect(thisDevice, context)
        }
    }

    private fun collectStaticBlocks(): List<BlockFactory<*>> {
        val staticBlocks =
            mutableListOf(
                // logic
                LogicAndBlockFactory(),
                LogicOrBlockFactory(),
                LogicNotBlockFactory(),
                LogicIfElseBlockFactory(),
                LogicDelayBlockFactory(),

                // triggers
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
            .filter { briefDto -> briefDto.name != null }
            .map { briefDto ->
                val id = briefDto.id
                val label = Resource.createUniResource(briefDto.name!!)
                ConditionBlockFactory(id, label)
            }
            .toList()
    }

    private fun collectSensorBlocks(): List<BlockFactory<*>> {
        val instanceBriefs = repository.getAllInstanceBriefs()
        val allConfigurables = pluginsCoordinator.configurables

        return instanceBriefs
            .asSequence()
            .filter { briefDto -> briefDto.name != null }
            .map { briefDto ->
                val configurable = allConfigurables.find { it.javaClass.name == briefDto.clazz }
                Pair(briefDto, configurable)
            }
            .filter { it.second is DeviceConfigurableWithBlockCategory<*> }
            .map { (briefDto, configurable) ->
                val id = briefDto.id
                val deviceConfigurable = configurable as DeviceConfigurableWithBlockCategory<*>
                val label = Resource.createUniResource(briefDto.name!!)
                val category = deviceConfigurable.blocksCategory

                SensorBlockFactory(deviceConfigurable.valueClazz, category, id, label)
            }
            .toList()
    }

    private fun collectChangeStateBlocks(thisDevice: Configurable): List<BlockFactory<*>> {
        if (thisDevice is StateDeviceConfigurable) {
            return thisDevice.states
                .filter { it.value.type == StateType.Control && it.value.action != null }
                .map { ChangeStateBlockFactory(it.value) }
                .toList()
        }

        return listOf()
    }

    private fun collectChangeValueBlocks(thisDevice: Configurable): List<BlockFactory<*>> {
        if (thisDevice is ControllerConfigurable<*>) {
            return listOf(ChangeValueBlockFactory(thisDevice.valueClazz))
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
                val configurableClazz =
                    allConfigurables.find { it.javaClass.name == briefDto.clazz }
                        as StateDeviceConfigurable
                StateChangeTriggerBlockFactory(instanceId, deviceName, configurableClazz.states)
            }
            .toList()
    }

    private fun collectPortUpdateTriggerBlock(): BlockFactory<*>? {
        val portIds = repository.getAllPorts().map { it.id }
        if (portIds.isNotEmpty()) {
            return PortUpdatedTriggerBlockFactory(portIds)
        }

        return null
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
                val configurableClazz =
                    allConfigurables.find { it.javaClass.name == briefDto.clazz }
                        as StateDeviceConfigurable
                StateValueBlockFactory(
                    deviceName,
                    instanceId,
                    configurableClazz.states,
                )
            }
            .toList()
    }
}
