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

package eu.automateeverything.domain.dependencies

import eu.automateeverything.data.Repository
import eu.automateeverything.data.fields.FieldType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.ResourceNotFoundException
import eu.automateeverything.domain.automation.Block
import eu.automateeverything.domain.automation.BlocklyParser
import eu.automateeverything.domain.automation.blocks.BlockFactoriesCollector
import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.domain.configurable.ConfigurableWithFields
import eu.automateeverything.domain.configurable.NameDescriptionConfigurable
import eu.automateeverything.domain.extensibility.PluginsCoordinator
import jakarta.inject.Inject

class DependencyChecker
@Inject
constructor(
    private val pluginsCoordinator: PluginsCoordinator,
    private val blocklyParser: BlocklyParser,
    private val blockFactoriesCollector: BlockFactoriesCollector,
    private val repository: Repository
) {
    fun checkInstance(instanceId: Long): HashMap<Long, Dependency> {
        val allInstances = repository.getAllInstances()

        val checkedInstance = findInstance(instanceId, allInstances)
        if (checkedInstance != null) {
            val dependencies = HashMap<Long, Dependency>()
            checkReferences(checkedInstance, allInstances, dependencies)
            checkAutomations(checkedInstance, allInstances, dependencies)
            return dependencies
        }

        throw ResourceNotFoundException()
    }

    private fun findInstance(instanceId: Long, allInstances: List<InstanceDto>): InstanceDto? {
        return allInstances.find { it.id == instanceId }
    }

    private fun findConfigurable(clazz: String): Configurable? {
        return pluginsCoordinator.configurables.find { configurable ->
            configurable.javaClass.name == clazz
        }
    }

    private fun findInstanceName(instanceId: Long, allInstances: List<InstanceDto>): String? {
        val instance = allInstances.find { instanceDto -> instanceDto.id == instanceId }
        return instance?.fields?.get(NameDescriptionConfigurable.FIELD_NAME)
    }

    private fun checkReferences(
        checkedInstance: InstanceDto,
        allInstances: List<InstanceDto>,
        dependencies: MutableMap<Long, Dependency>
    ) {
        fun addDependency(instanceId: Long, dependency: Dependency) {
            if (!dependencies.containsKey(instanceId)) {
                dependencies[instanceId] = dependency
                val instanceToCheck =
                    allInstances.find { instanceDto -> instanceDto.id == instanceId }
                if (instanceToCheck != null) {
                    checkReferences(instanceToCheck, allInstances, dependencies)
                    checkAutomations(instanceToCheck, allInstances, dependencies)
                }
            }
        }

        allInstances.forEach { instance ->
            val configurable = findConfigurable(instance.clazz)
            if (configurable is ConfigurableWithFields) {
                configurable.fieldDefinitions.values.forEach { fieldDefinition ->
                    if (fieldDefinition.type == FieldType.InstanceReference) {
                        val instanceIds = instance.fields[fieldDefinition.name]
                        instanceIds
                            ?.split(",")
                            ?.map { it.toLong() }
                            ?.forEach { instanceId ->
                                if (checkedInstance.id == instanceId) {
                                    val name = findInstanceName(instance.id, allInstances)
                                    if (name != null) {
                                        addDependency(
                                            instanceId,
                                            Dependency(instance.id, DependencyType.Instance, name)
                                        )
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

    private fun checkAutomations(
        checkedInstance: InstanceDto,
        allInstances: List<InstanceDto>,
        dependencies: MutableMap<Long, Dependency>
    ) {
        allInstances.forEach { instanceDto ->
            val automation = instanceDto.automation
            if (automation != null) {
                val configurable = findConfigurable(instanceDto.clazz)!!
                val factories = blockFactoriesCollector.collect(configurable)
                val xml = blocklyParser.parse(automation)
                xml.blocks?.forEach { triggerBlock ->
                    traverseBlock(triggerBlock) { block ->
                        val relatedFactory =
                            factories.find { factory -> factory.type == block.type }
                        relatedFactory?.dependsOn()?.forEach { relatedInstanceId ->
                            if (checkedInstance.id == relatedInstanceId) {
                                val name = findInstanceName(instanceDto.id, allInstances)
                                dependencies[instanceDto.id] =
                                    Dependency(instanceDto.id, DependencyType.Automation, name)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun traverseBlock(block: Block, blockFound: (Block) -> Unit) {
        fun foundAndTraverse(block: Block) {
            blockFound(block)
            traverseBlock(block, blockFound)
        }

        if (block.next?.block != null) {
            foundAndTraverse(block.next.block)
        }

        block.values?.forEach { value ->
            if (value.block != null) {
                foundAndTraverse(value.block)
            }
        }

        block.statements?.forEach { statement ->
            if (statement.block != null) {
                foundAndTraverse(statement.block)
            }
        }
    }
}
