package eu.geekhome

import eu.geekhome.automation.AutomationConductor
import eu.geekhome.automation.blocks.BlockFactoriesCollector
import eu.geekhome.rest.*
import eu.geekhome.services.events.NumberedEventsSink
import org.glassfish.jersey.server.ResourceConfig

open class App : ResourceConfig() {
    init {
        val liveEvents = NumberedEventsSink()

        val pluginsCoordinator: PluginsCoordinator = SingletonExtensionsPluginsCoordinator(liveEvents)
        pluginsCoordinator.loadPlugins()

        val hardwareManager = HardwareManager(pluginsCoordinator, liveEvents)
        val blockFactoriesCoordinator = BlockFactoriesCollector(pluginsCoordinator)
        val automationConductor = AutomationConductor(hardwareManager, blockFactoriesCoordinator, pluginsCoordinator, liveEvents)

        packages("eu.geekhome.rest")
        register(liveEvents)
        register(DependencyInjectionBinder())
        register(GsonMessageBodyHandler())
        register(CORSFilter())
        register(ResourceNotFoundExceptionMapper())
        register(pluginsCoordinator)
        register(hardwareManager)
        register(automationConductor)
        register(blockFactoriesCoordinator)

        pluginsCoordinator.startPlugins()
    }
}