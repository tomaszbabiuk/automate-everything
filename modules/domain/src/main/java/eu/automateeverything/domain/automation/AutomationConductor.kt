package eu.automateeverything.domain.automation

import eu.automateeverything.data.Repository
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.automation.State
import eu.automateeverything.domain.hardware.HardwareManager
import eu.automateeverything.domain.extensibility.PluginsCoordinator
import eu.automateeverything.domain.WithStartStopScope
import eu.automateeverything.domain.automation.blocks.BlockFactoriesCollector
import eu.automateeverything.domain.R
import eu.automateeverything.domain.configurable.ConditionConfigurable
import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.domain.configurable.SensorConfigurable
import eu.automateeverything.domain.configurable.StateDeviceConfigurable
import eu.automateeverything.domain.events.*
import eu.automateeverything.domain.inbox.Inbox
import kotlinx.coroutines.*
import java.util.*

class AutomationConductor(
    private val hardwareManager: HardwareManager,
    private val blockFactoriesCollector: BlockFactoriesCollector,
    private val pluginsCoordinator: PluginsCoordinator,
    private val liveEvents: EventsSink,
    private val inbox: Inbox,
    private val repository: Repository,
    private val stateChangeReporter: StateChangeReporter
) : WithStartStopScope(), LiveEventsListener {

    init {
        liveEvents.addAdapterEventListener(this)
    }

    private var automationJob: Job? = null
    private var enabled: Boolean = false
    private var firstLoop: Boolean = false
    private val blocklyParser = BlocklyParser()
    private val blocklyTransformer = BlocklyTransformer()
    val automationUnitsCache = HashMap<Long, Pair<InstanceDto, DeviceAutomationUnit<*>>>()
    private val evaluationUnitsCache = HashMap<Long, EvaluableAutomationUnit>()

    fun isEnabled(): Boolean {
        return enabled
    }

    private fun broadcastAutomationUpdate() {
        liveEvents.broadcastEvent(AutomationStateEventData(enabled))
        if (enabled) {
            inbox.sendAutomationStarted()
        } else {
            inbox.sendAutomationStopped()
        }
    }

    fun enable() {
        if (!enabled) {
            enabled = true
            broadcastAutomationUpdate()
            automationUnitsCache.clear()
            evaluationUnitsCache.clear()

            println("Enabling automation")
            start()
        } else {
            stop()
        }
    }

    private fun rebuildAutomations(): Map<Long, List<StatementNode>> {
        firstLoop = true

        stateChangeReporter.removeAllListeners()

        val allInstances = repository.getAllInstances()
        val allConfigurables = pluginsCoordinator.configurables

        allInstances.forEach { instance ->
            val configurable = allConfigurables.find { instance.clazz == it.javaClass.name }
            if (configurable != null) {

                if (configurable is ConditionConfigurable) {
                    val evaluator = configurable.buildEvaluator(instance)
                    evaluationUnitsCache[instance.id] = evaluator
                } else {
                    try {
                        val physicalUnit = buildPhysicalUnit(configurable, instance)
                        automationUnitsCache[instance.id] = Pair(instance, physicalUnit)
                    } catch (ex: AutomationErrorException) {
                        println(ex)
                        val originName = instance.fields["name"]
                        val wrapper = buildWrappedUnit(originName, configurable, ex)
                        automationUnitsCache[instance.id] = Pair(instance, wrapper)
                    } catch (ex: Exception) {
                        println(ex)
                        val originName = instance.fields["name"]
                        val aex = AutomationErrorException(R.error_automation, ex)
                        val wrapper = buildWrappedUnit(originName, configurable, aex)
                        automationUnitsCache[instance.id] = Pair(instance, wrapper)
                    }
                }
            }
        }

        return allInstances
            .filter { it.automation != null }
            .associateBy({ it.id}, { instanceDto ->
                val thisDevice = allConfigurables
                    .find { configurable -> configurable.javaClass.name == instanceDto.clazz }

                val blocksCache = blockFactoriesCollector.collect(thisDevice)
                val context =
                    AutomationContext(
                        instanceDto, thisDevice,
                        automationUnitsCache.mapValues { it.value.second },
                        evaluationUnitsCache,
                        blocksCache,
                        stateChangeReporter
                    )

                val blocklyXml = blocklyParser.parse(instanceDto.automation!!)
                if (blocklyXml.blocks != null) {
                    blocklyTransformer.transform(blocklyXml.blocks, context)
                } else {
                    listOf()
                }
            })
    }

    private fun buildPhysicalUnit(configurable: Configurable, instance: InstanceDto): DeviceAutomationUnit<*> {
        return when (configurable) {
            is StateDeviceConfigurable -> {
                configurable.buildAutomationUnit(instance)
            }

            is SensorConfigurable<*> -> {
                configurable.buildAutomationUnit(instance)
            }

            else -> throw Exception("Unsupported configurable type, can this configurable be automated?")
        }
    }

    private fun buildWrappedUnit(originName: String?, configurable: Configurable, ex: AutomationErrorException): AutomationUnitWrapper<*> {
        return when (configurable) {
            is StateDeviceConfigurable -> {
                AutomationUnitWrapper(originName, State::class.java, ex)
            }

            is SensorConfigurable<*> -> {
                AutomationUnitWrapper(originName, configurable.valueClazz, ex)
            }

            else -> throw Exception("Unsupported configurable type, can this configurable be automated?")
        }
    }

    override fun start() {
        super.start()

        var automations = rebuildAutomations()

        automationJob = startStopScope.launch {
            while (isActive) {
                val now = Calendar.getInstance()

                val hasAutomations = automations.isNotEmpty()
                val hasNewPorts = hardwareManager.checkNewPorts()
                val hasUpdatedInstance = repository.hasUpdatedInstance()

                if (hasNewPorts || hasUpdatedInstance) {
                    if (hasNewPorts) {
                        println("Hardware manager has new ports... rebuilding automation")
                        hardwareManager.clearNewPortsFlag()
                    }

                    if (hasUpdatedInstance) {
                        println("Repository has updated instance... rebuilding automation")
                        repository.clearInstanceUpdatedFlag()
                    }

                    automations = rebuildAutomations()
                }

                automationUnitsCache
                    .map { it.value.second }
                    .filter { it.recalculateOnTimeChange }
                    .forEach { it.calculate(now) }

                if (hasAutomations) {
                    println("Processing maintenance + automation loop")

                    automations
                        .forEach { (instanceId,automationList) ->
                            automationList.forEach {
                                try {
                                    it.process(now, firstLoop)
                                } catch (ex: AutomationErrorException) {
                                    println("Exception during automation $instanceId")
                                    automationUnitsCache[instanceId]!!.second.markExternalError(ex)
                                }
                            }
                        }

                    firstLoop = false
                    hardwareManager.executeAllPendingChanges()
                } else {
                    println("Processing maintenance loop")
                }

                hardwareManager.afterAutomationLoop()
                delay(1000)
            }
        }
    }

    fun disable() {
        automationJob?.cancel("Disabling automation")
        enabled = false
        automationUnitsCache.clear()
        evaluationUnitsCache.clear()
        println("Disabling automation")

        broadcastAutomationUpdate()
    }

    override fun onEvent(event: LiveEvent<*>) {
        val now = Calendar.getInstance()
        if (event.data is PortUpdateEventData) {
            val port = event.data.port
            automationUnitsCache
                .filter {
                    val unit = it.value.second
                    unit.recalculateOnPortUpdate && unit.usedPortsIds.contains(port.id)
                }
                .forEach {
                    val unit = it.value.second
                    val lastEvaluation = unit.lastEvaluation
                    unit.calculate(now)
                    val newEvaluation = unit.lastEvaluation
                    if (newEvaluation != lastEvaluation) {
                        val instance = it.value.first
                        val eventData = AutomationUpdateEventData(unit, instance, newEvaluation)
                        liveEvents.broadcastEvent(eventData)
                    }
                }
        }
    }
}