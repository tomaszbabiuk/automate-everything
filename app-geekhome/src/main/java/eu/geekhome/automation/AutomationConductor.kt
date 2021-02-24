package eu.geekhome.automation

import eu.geekhome.HardwareManager
import eu.geekhome.rest.getConfigurables
import eu.geekhome.rest.getRepository
import eu.geekhome.services.hardware.IPortFinder
import eu.geekhome.services.repository.InstanceDto
import org.pf4j.PluginManager
import eu.geekhome.services.configurable.Configurable

class AutomationContext(
    val instanceDto: InstanceDto,
    val configurable: Configurable?,
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

            val automations = repository
                .getAllInstances()
                .filter { it.automation != null }
                .map { instanceDto ->
                    val configurable = pluginsCoordinator
                        .getConfigurables()
                        .find { configurable -> configurable.javaClass.name == instanceDto.clazz }

                    val context = AutomationContext(instanceDto, configurable, hardwareManager)

                    val blocklyXml = blocklyParser.parse(instanceDto.automation!!)
                    blocklyTransformer.transform(blocklyXml, context)
                }

            println(automations)
        }
    }

    fun disable() {
        enabled = false
        println("Disabling automation")
    }
}