package eu.geekhome.automation

import eu.geekhome.HardwareManager
import eu.geekhome.PluginsCoordinator
import eu.geekhome.automation.blocks.BlockFactoriesCollector
import eu.geekhome.domain.automation.DeviceAutomationUnit
import eu.geekhome.domain.automation.IEvaluableAutomationUnit
import eu.geekhome.domain.automation.State
import eu.geekhome.domain.configurable.ConditionConfigurable
import eu.geekhome.domain.configurable.Configurable
import eu.geekhome.domain.configurable.SensorConfigurable
import eu.geekhome.domain.configurable.StateDeviceConfigurable
import eu.geekhome.domain.events.*
import eu.geekhome.domain.repository.InstanceDto
import eu.geekhome.sqldelightplugin.SqlDelightRepository
import kotlinx.coroutines.*
import java.util.*

class AutomationConductor(
    private val hardwareManager: HardwareManager,
    private val blockFactoriesCollector: BlockFactoriesCollector,
    private val pluginsCoordinator: PluginsCoordinator,
    private val liveEvents: NumberedEventsSink,
    private val repository: SqlDelightRepository
) : LiveEventsListener {

    init {
        liveEvents.addAdapterEventListener(this)
    }

    private var automationJob: Job? = null
    private var enabled: Boolean = false
    private var checkingNewPortsTime = 0L
    private val blocklyParser = BlocklyParser()
    private val blocklyTransformer = BlocklyTransformer()
    val automationUnitsCache = HashMap<Long, Pair<InstanceDto, DeviceAutomationUnit<*>>>()
    private val evaluationUnitsCache = HashMap<Long, IEvaluableAutomationUnit>()

    fun isEnabled(): Boolean {
        return enabled
    }

    private fun broadcastAutomationUpdate() {
        liveEvents.broadcastEvent(AutomationStateEventData(enabled))
    }

    fun enable() {
        if (!enabled) {
            enabled = true
            broadcastAutomationUpdate()
            automationUnitsCache.clear()
            evaluationUnitsCache.clear()

            println("Enabling automation")
            startAutomations()
        }
    }

    private fun rebuildAutomations(): List<List<IStatementNode>> {

        val allInstances = repository.getAllInstances()
        val allConfigurables = pluginsCoordinator.configurables

        allInstances.forEach { instance ->
            val configurable = allConfigurables.find { instance.clazz == it.javaClass.name }
            if (configurable != null) {
                try {
                    val physicalUnit = buildPhysicalUnit(configurable, instance)
                    automationUnitsCache[instance.id] = Pair(instance, physicalUnit)
                } catch (ex: Exception) {
                    val wrapper = buildWrappedUnit(configurable, ex)
                    automationUnitsCache[instance.id] = Pair(instance, wrapper)
                }
            }

            if (configurable is ConditionConfigurable) {
                val evaluator = configurable.buildEvaluator(instance)
                evaluationUnitsCache[instance.id] = evaluator
            }
        }

        return allInstances
            .filter { it.automation != null }
            .map { instanceDto ->
                val thisDevice = allConfigurables
                    .find { configurable -> configurable.javaClass.name == instanceDto.clazz }

                val blocksCache = blockFactoriesCollector.collect(thisDevice)
                val context =
                    AutomationContext(
                        instanceDto, thisDevice,
                        automationUnitsCache.mapValues { it.value.second },
                        evaluationUnitsCache,
                        blocksCache,
                        liveEvents
                    )

                val blocklyXml = blocklyParser.parse(instanceDto.automation!!)
                blocklyTransformer.transform(blocklyXml, context)
            }
    }

    private fun buildPhysicalUnit(configurable: Configurable, instance: InstanceDto): DeviceAutomationUnit<*> {
        return when (configurable) {
            is StateDeviceConfigurable -> {
                configurable.buildAutomationUnit(instance, hardwareManager)
            }

            is SensorConfigurable<*> -> {
                configurable.buildAutomationUnit(instance, hardwareManager)
            }

            else -> throw Exception("Unsupported configurable type, can this configurable be automated?")
        }
    }

    private fun buildWrappedUnit(configurable: Configurable, ex: Exception): AutomationUnitWrapper<*> {
        return when (configurable) {
            is StateDeviceConfigurable -> {
                AutomationUnitWrapper(State::class.java, ex)
            }

            is SensorConfigurable<*> -> {
                AutomationUnitWrapper(configurable.valueType, ex)
            }

            else -> throw Exception("Unsupported configurable type, can this configurable be automated")
        }
    }

    private fun startAutomations() {
        val automations = rebuildAutomations()

        hardwareManager.checkNewPorts()
        checkingNewPortsTime = Calendar.getInstance().timeInMillis

        val triggers = automations.flatten()

        var hasNewPorts = false
        var beforeAutomationLoopTask: Deferred<Unit>? = null
        automationJob = GlobalScope.launch {
            while (isActive && !hasNewPorts) {
                val now = Calendar.getInstance()
                if (beforeAutomationLoopTask == null || beforeAutomationLoopTask!!.isCompleted) {
                    beforeAutomationLoopTask = async {
                        hardwareManager.beforeAutomationLoop(now)
                    }
                } else {
                    println("Skipping before automation loop call (busy)")
                }

                if (automations.isNotEmpty()) {
                    println("Processing automation loop")
                    triggers.forEach {
                        it.process(now)
                    }
                }

                println("Processing maintenance loop")
                if (now.timeInMillis > checkingNewPortsTime + CHECKING_NEW_PORTS_INTERVAL) {
                    hasNewPorts = hardwareManager.checkNewPorts()
                    if (hasNewPorts) {
                        println("Has new ports, automation has to be restarted")
                    }
                }

                hardwareManager.afterAutomationLoop()
                delay(1000)
            }

            println("Automation stopped")
            if (hasNewPorts) {
                println("Restarting, reason = HAS NEW PORTS")
                startAutomations()
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
            val port = (event.data as PortUpdateEventData).port
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

    companion object {
        const val CHECKING_NEW_PORTS_INTERVAL = 15000L
    }
}