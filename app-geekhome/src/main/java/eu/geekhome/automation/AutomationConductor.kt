package eu.geekhome.automation

import eu.geekhome.HardwareManager
import eu.geekhome.automation.blocks.BlockFactoriesCollector
import eu.geekhome.rest.getConfigurables
import eu.geekhome.rest.getRepository
import eu.geekhome.services.automation.IDeviceAutomationUnit
import eu.geekhome.services.automation.IEvaluableAutomationUnit
import eu.geekhome.services.automation.State
import eu.geekhome.services.automation.UnitCondition
import eu.geekhome.services.configurable.*
import eu.geekhome.services.repository.InstanceDto
import org.pf4j.PluginManager
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
    val pluginsCoordinator: PluginManager) {

    private var automationJob: Job? = null
    private var enabled: Boolean = false
    private val blocklyParser = BlocklyParser()
    private val blocklyTransformer = BlocklyTransformer()
    val automationUnitsCache = HashMap<Long, Pair<InstanceDto, IDeviceAutomationUnit<*>>>()
    private val evaluationUnitsCache = HashMap<Long, IEvaluableAutomationUnit>()

    fun isEnabled(): Boolean {
        return enabled
    }

    fun enable() {
        if (!enabled) {
            enabled = true
            automationUnitsCache.clear()
            evaluationUnitsCache.clear()

            println("Enabling automation")

            val repository = pluginsCoordinator.getRepository()
            val allInstances = repository.getAllInstances()
            val allConfigurables = pluginsCoordinator.getConfigurables()

            allInstances.forEach { instance ->
                val configurable = allConfigurables.find { instance.clazz == it.javaClass.name}
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

            val automations = allInstances
                .filter { it.automation != null }
                .map { instanceDto ->
                    val thisDevice = allConfigurables
                        .find { configurable -> configurable.javaClass.name == instanceDto.clazz }

                    val blocksCache = blockFactoriesCollector.collect(thisDevice)
                    val context =
                        AutomationContext(instanceDto, thisDevice,
                            automationUnitsCache.mapValues { it.value.second },
                            evaluationUnitsCache, blocksCache)

                    val blocklyXml = blocklyParser.parse(instanceDto.automation!!)
                    blocklyTransformer.transform(blocklyXml, context)
                }

            startAutomations(automations)
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

    private fun startAutomations(automations: List<List<IStatementNode>>) {
        hardwareManager.checkNewPorts()

        if (automations.isNotEmpty()) {
            val triggers = automations
                .flatMap { it }

            automationJob = GlobalScope.launch {
                while (isActive) {
                    val now = Calendar.getInstance()
                    triggers.forEach {
                        println("Processing automation")
                        it.process(now)
                        delay(1000)
                    }

                    hardwareManager.afterAutomationLoop(now)
                }

                println("Automation stopped")
            }
        }
    }

    fun disable() {
        automationJob?.cancel("Disabling automation")
        enabled = false
        automationUnitsCache.clear()
        evaluationUnitsCache.clear()
        println("Disabling automation")
    }
}