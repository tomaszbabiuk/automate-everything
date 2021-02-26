package eu.geekhome.automation

import eu.geekhome.HardwareManager
import eu.geekhome.rest.getConfigurables
import eu.geekhome.rest.getRepository
import eu.geekhome.services.hardware.IPortFinder
import eu.geekhome.services.repository.InstanceDto
import org.pf4j.PluginManager
import eu.geekhome.services.configurable.Configurable
import java.util.*

class AutomationContext(
    val instanceDto: InstanceDto,
    val thisDevice: Configurable?,
    val allInstances: List<InstanceDto>,
    val allConfigurables: List<Configurable>,
    val portFinder: IPortFinder
)

class AutomationConductor(
    val hardwareManager: HardwareManager,
    val pluginsCoordinator: PluginManager) {

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

            println(automations)

            automations
                .flatMap { it.triggers }
                .forEach { it.process(Calendar.getInstance()) }
        }
    }

    fun disable() {
        enabled = false
        println("Disabling automation")
    }
}