package eu.geekhome.automation

import eu.geekhome.HardwareManager
import eu.geekhome.rest.getConfigurables
import eu.geekhome.rest.getRepository
import eu.geekhome.services.hardware.IPortFinder
import eu.geekhome.services.repository.InstanceDto
import org.pf4j.PluginManager
import eu.geekhome.services.configurable.Configurable
import kotlinx.coroutines.*
import java.util.*

class AutomationContext(
    val instanceDto: InstanceDto,
    val thisDevice: Configurable?,
    val allInstances: List<InstanceDto>,
    val allConfigurables: List<Configurable>,
    val portFinder: IPortFinder
)

class AutomationConductor(
    private val hardwareManager: HardwareManager,
    val pluginsCoordinator: PluginManager) {

    private var automationJob: Job? = null
    private var enabled: Boolean = false
    private val blocklyParser = BlocklyParser()
    private val blocklyTransformer = BlocklyTransformer()

    fun isEnabled(): Boolean {
        return enabled
    }

    fun enable() {
        if (!enabled) {
            enabled = true
            println("Enabling automation")

            val repository = pluginsCoordinator.getRepository()

            val allInstances = repository.getAllInstances()
            val allConfigurables = pluginsCoordinator.getConfigurables()

            val automations = allInstances
                .filter { it.automation != null }
                .map { instanceDto ->
                    val thisDevice = allConfigurables
                        .find { configurable -> configurable.javaClass.name == instanceDto.clazz }

                    val context =
                        AutomationContext(instanceDto, thisDevice, allInstances, allConfigurables, hardwareManager)

                    val blocklyXml = blocklyParser.parse(instanceDto.automation!!)
                    blocklyTransformer.transform(blocklyXml, context)
                }

            startAutomations(automations)
        }
    }

    private fun startAutomations(automations: List<AutomationPack>) {
        if (automations.isNotEmpty()) {
            val triggers = automations
                .flatMap { it.triggers }

            automationJob = GlobalScope.launch {
                while (isActive) {
                    val now = Calendar.getInstance()
                    triggers.forEach {
                        println("Processing automation")
                        it.process(now)
                        delay(1000)
                    }

                    hardwareManager.executePendingChanges()
                }

                println("Automation stopped")
            }
        }
    }

    fun disable() {
        automationJob?.cancel("Disabling automation")
        enabled = false
        println("Disabling automation")
    }
}