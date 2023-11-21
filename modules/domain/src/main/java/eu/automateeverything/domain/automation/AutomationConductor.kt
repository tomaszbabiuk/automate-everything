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

package eu.automateeverything.domain.automation

import eu.automateeverything.data.DataRepository
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.R
import eu.automateeverything.domain.WithStartStopScope
import eu.automateeverything.domain.automation.blocks.BlockFactoriesCollector
import eu.automateeverything.domain.automation.blocks.CollectionContext
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.domain.events.*
import eu.automateeverything.domain.extensibility.PluginsCoordinator
import eu.automateeverything.domain.hardware.HardwareManager
import eu.automateeverything.domain.inbox.Inbox
import java.util.*
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory

class AutomationConductor(
    private val hardwareManager: HardwareManager,
    private val blockFactoriesCollector: BlockFactoriesCollector,
    private val pluginsCoordinator: PluginsCoordinator,
    private val eventBus: EventBus,
    private val inbox: Inbox,
    private val dataRepository: DataRepository
) : WithStartStopScope<Void?>(), LiveEventsListener {

    init {
        eventBus.subscribeToGlobalEvents(this)
    }

    private val logger = LoggerFactory.getLogger(AutomationConductor::class.java)
    private var automationJob: Job? = null
    private var enabled: Boolean = false
    private var firstLoop: Boolean = false
    private val blocklyParser = BlocklyParser()
    private val blocklyTransformer = BlocklyTransformer()
    val automationUnitsCache = HashMap<Long, Pair<InstanceDto, AutomationUnit<*>>>()
    private val evaluationUnitsCache = HashMap<Long, EvaluableAutomationUnit>()

    fun isEnabled(): Boolean {
        return enabled
    }

    private fun broadcastAutomationUpdate() {
        eventBus.broadcastAutomationStateChange(enabled)
        if (enabled) {
            inbox.sendMessage(
                R.inbox_message_automation_enabled_subject,
                R.inbox_message_automation_enabled_body
            )
        } else {
            inbox.sendMessage(
                R.inbox_message_automation_disabled_subject,
                R.inbox_message_automation_disabled_body
            )
        }
    }

    fun enable() {
        if (!enabled) {
            enabled = true
            broadcastAutomationUpdate()
            automationUnitsCache.clear()
            evaluationUnitsCache.clear()

            logger.info("Enabling automation")
            start(null)
        } else {
            stop()
        }
    }

    private fun rebuildAutomations(): Map<Long, List<StatementNode>> {
        firstLoop = true

        eventBus.unsubscribeFromStateChanges()

        val allInstances = dataRepository.getAllInstances()
        val allConfigurables = pluginsCoordinator.configurables

        allInstances.forEach { instance ->
            val configurable = allConfigurables.find { instance.clazz == it.javaClass.name }
            if (configurable != null) {
                if (configurable is ConditionConfigurable) {
                    val evaluator = configurable.buildEvaluator(instance)
                    evaluationUnitsCache[instance.id] = evaluator
                } else if (configurable is DeviceConfigurable<*>) {
                    try {
                        val physicalUnit = buildPhysicalUnit(configurable, instance)
                        automationUnitsCache[instance.id] = Pair(instance, physicalUnit)
                    } catch (ex: AutomationErrorException) {
                        val originName = instance.fields[NameDescriptionConfigurable.FIELD_NAME]
                        val wrapper = buildWrappedUnit(originName, instance, configurable, ex)
                        automationUnitsCache[instance.id] = Pair(instance, wrapper)
                    } catch (ex: Exception) {
                        val originName = instance.fields[NameDescriptionConfigurable.FIELD_NAME]
                        val aex = AutomationErrorException(R.error_automation(ex), ex)
                        val wrapper = buildWrappedUnit(originName, instance, configurable, aex)
                        automationUnitsCache[instance.id] = Pair(instance, wrapper)
                    }
                }
            } else {
                val originName = instance.fields[NameDescriptionConfigurable.FIELD_NAME]
                val name = originName ?: "-------"
                val initError = AutomationErrorException(R.error_device_missing(instance.clazz))
                val wrapper =
                    AutomationUnitWrapper(Nothing::class.java, eventBus, name, instance, initError)
                automationUnitsCache[instance.id] = Pair(instance, wrapper)
            }
        }

        automationUnitsCache.values.forEach { it.second.bind(automationUnitsCache) }

        return allInstances
            .filter { it.automation != null }
            .associateBy(
                { it.id },
                { instanceDto ->
                    val thisDevice =
                        allConfigurables.find { configurable ->
                            configurable.javaClass.name == instanceDto.clazz
                        }

                    val factoriesCache =
                        blockFactoriesCollector.collect(
                            thisDevice!!,
                            instanceDto.id,
                            CollectionContext.Automation
                        )
                    val context =
                        AutomationContext(
                            instanceDto,
                            thisDevice,
                            automationUnitsCache.mapValues { it.value.second },
                            evaluationUnitsCache,
                            factoriesCache,
                            eventBus
                        )

                    val blocklyXml = blocklyParser.parse(instanceDto.automation!!)
                    if (blocklyXml.blocks != null) {
                        blocklyTransformer.transform(blocklyXml.blocks, context)
                    } else {
                        listOf()
                    }
                }
            )
    }

    private fun buildPhysicalUnit(
        configurable: Configurable,
        instance: InstanceDto
    ): AutomationUnit<*> {
        return when (configurable) {
            is StateDeviceConfigurable -> {
                configurable.buildAutomationUnit(instance)
            }
            is DeviceConfigurable<*> -> {
                configurable.buildAutomationUnit(instance)
            }
            else ->
                throw Exception(
                    "Unsupported configurable type, can this configurable be automated?"
                )
        }
    }

    private fun buildWrappedUnit(
        originName: String?,
        instance: InstanceDto,
        configurable: Configurable,
        ex: AutomationErrorException
    ): AutomationUnitWrapper<*> {
        val name = originName ?: "-----"

        return when (configurable) {
            is StateDeviceConfigurable -> {
                StateDeviceAutomationUnitWrapper(eventBus, instance, name, ex)
            }
            is ControllerAutomationUnit<*> -> {
                ControllerAutomationUnitWrapper(
                    configurable.valueClazz,
                    eventBus,
                    name,
                    instance,
                    ex
                )
            }
            is DeviceConfigurable<*> -> {
                AutomationUnitWrapper(configurable.valueClazz, eventBus, name, instance, ex)
            }
            else -> {
                throw Exception(
                    "Unsupported configurable type, can this configurable be automated?"
                )
            }
        }
    }

    override fun start(params: Void?) {
        super.start(params)

        hardwareManager.clearNewPortsFlag()
        var automations = rebuildAutomations()

        automationJob =
            startStopScope.launch {
                while (isActive) {
                    val now = Calendar.getInstance()

                    val hasAutomations = automations.isNotEmpty()
                    val hasNewPorts = hardwareManager.checkNewPorts()
                    val hasUpdatedInstance = dataRepository.hasUpdatedInstance()

                    if (hasNewPorts || hasUpdatedInstance) {
                        if (hasNewPorts) {
                            logger.debug("Hardware manager has new ports... rebuilding automation")
                            hardwareManager.clearNewPortsFlag()
                        }

                        if (hasUpdatedInstance) {
                            logger.debug("Repository has updated instance... rebuilding automation")
                            dataRepository.clearInstanceUpdatedFlag()
                        }

                        automations = rebuildAutomations()
                    }

                    automationUnitsCache
                        .map { it.value.second }
                        .filter { it.recalculateOnTimeChange }
                        .forEach { it.calculate(now) }

                    if (hasAutomations) {
                        logger.debug("Processing maintenance + automation loop")

                        automations.forEach { (instanceId, automationList) ->
                            automationList.forEach {
                                try {
                                    it.process(now, firstLoop)
                                } catch (ex: AutomationErrorException) {
                                    logger.error("Exception during automation $instanceId", ex)
                                    automationUnitsCache[instanceId]!!.second.markExternalError(ex)
                                }
                            }
                        }

                        firstLoop = false
                        hardwareManager.executeAllPendingChanges()
                    } else {
                        logger.debug("Processing maintenance loop")
                    }

                    hardwareManager.afterAutomationLoop()
                    delay(1000)
                }

                logger.warn("Automation job not longer active")
            }
    }

    fun disable() {
        automationJob?.cancel("Disabling automation")
        enabled = false
        automationUnitsCache.clear()
        evaluationUnitsCache.clear()
        logger.debug("Disabling automation")

        broadcastAutomationUpdate()
    }

    override fun onEvent(event: LiveEvent<*>) {
        val now = Calendar.getInstance()
        if (event.data is PortUpdateEventData && event.data.type == PortUpdateType.ValueChange) {
            val port = event.data.port
            automationUnitsCache
                .filter {
                    val unit = it.value.second
                    unit.recalculateOnPortUpdate && unit.usedPortsIds.contains(port.portId)
                }
                .forEach {
                    val unit = it.value.second
                    unit.calculate(now)
                }
        }
    }
}
