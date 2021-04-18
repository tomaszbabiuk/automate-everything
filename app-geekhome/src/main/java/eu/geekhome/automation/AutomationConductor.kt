package eu.geekhome.automation

import eu.geekhome.HardwareManager
import eu.geekhome.PluginsCoordinator
import eu.geekhome.automation.blocks.BlockFactoriesCollector
import eu.geekhome.services.automation.IDeviceAutomationUnit
import eu.geekhome.services.automation.IEvaluableAutomationUnit
import eu.geekhome.services.automation.State
import eu.geekhome.services.automation.UnitCondition
import eu.geekhome.services.configurable.*
import eu.geekhome.services.events.*
import eu.geekhome.services.repository.InstanceDto
import kotlinx.coroutines.*
import java.util.*

class AutomationContext(
    val instanceDto: InstanceDto,
    val thisDevice: Configurable?,
    val automationUnitsCache: Map<Long, IDeviceAutomationUnit<*>>,
    val evaluationUnitsCache: Map<Long, IEvaluableAutomationUnit>,
    val blocksCache: List<BlockFactory<*>>,
)

class AutomationConductor(
    private val hardwareManager: HardwareManager,
    private val blockFactoriesCollector: BlockFactoriesCollector,
    private val pluginsCoordinator: PluginsCoordinator,
    private val liveEvents: NumberedEventsSink
) : LiveEventsListener {

    init {
        liveEvents.addAdapterEventListener(this)
    }

    private var automationJob: Job? = null
    private var enabled: Boolean = false
    private var checkingNewPortsTime = 0L
    private val blocklyParser = BlocklyParser()
    private val blocklyTransformer = BlocklyTransformer()
    val automationUnitsCache = HashMap<Long, Pair<InstanceDto, IDeviceAutomationUnit<*>>>()
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

    fun rebuildAutomations(): List<List<IStatementNode>> {

        val repository = pluginsCoordinator.repository
        val allInstances = repository.getAllInstances()
        val allConfigurables = pluginsCoordinator.configurables

        allInstances.forEach { instance ->
            val configurable = allConfigurables.find { instance.clazz == it.javaClass.name }
            if (configurable != null) {
                try {
                    val physicalUnit = buildPhysicalUnit(configurable, instance)
                    automationUnitsCache[instance.id] = Pair(instance, physicalUnit)
                } catch (ex: Exception) {
                    val wrapper = buildWrappedUnit(configurable)
                    wrapper.error = ex
                    wrapper.condition = UnitCondition.InitError
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
                        evaluationUnitsCache, blocksCache
                    )

                val blocklyXml = blocklyParser.parse(instanceDto.automation!!)
                blocklyTransformer.transform(blocklyXml, context)
            }
    }

    private fun buildPhysicalUnit(configurable: Configurable, instance: InstanceDto): IDeviceAutomationUnit<*> {
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

    private fun buildWrappedUnit(configurable: Configurable): AutomationUnitWrapper<*> {
        return when (configurable) {
            is StateDeviceConfigurable -> {
                AutomationUnitWrapper(State::class.java)
            }

            is SensorConfigurable<*> -> {
                AutomationUnitWrapper(configurable.valueType)
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
            automationJob = GlobalScope.launch {
            while (isActive && !hasNewPorts) {
                val now = Calendar.getInstance()
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

                hardwareManager.afterAutomationLoop(now)
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
                    unit.usedPortsIds.contains(port.id)
                }
                .forEach {
                    val unit = it.value.second
                    unit.calculate(now)

                    val instance = it.value.first
                    val eventData = AutomationUpdateEventData(unit, instance)
                    liveEvents.broadcastEvent(eventData)
                }

        }
    }

    companion object {
        const val CHECKING_NEW_PORTS_INTERVAL = 15000L
    }
}