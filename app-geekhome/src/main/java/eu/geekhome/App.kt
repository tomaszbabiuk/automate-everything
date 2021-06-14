package eu.geekhome

import eu.geekhome.automation.AutomationConductor
import eu.geekhome.automation.blocks.BlockFactoriesCollector
import eu.geekhome.rest.*
import eu.geekhome.domain.events.NumberedEventsSink
import eu.geekhome.sqldelightplugin.SqlDelightRepository
import org.glassfish.jersey.server.ResourceConfig

open class App : ResourceConfig() {
    init {
        val liveEvents = NumberedEventsSink()
        val repository = SqlDelightRepository()
        val pluginsCoordinator: PluginsCoordinator = SingletonExtensionsPluginsCoordinator(liveEvents)
        pluginsCoordinator.loadPlugins()

        val hardwareManager = HardwareManager(pluginsCoordinator, liveEvents, repository)
        val blockFactoriesCoordinator = BlockFactoriesCollector(pluginsCoordinator, repository)
        val automationConductor = AutomationConductor(hardwareManager, blockFactoriesCoordinator, pluginsCoordinator,
            liveEvents, repository)

        packages("eu.geekhome.rest")
        register(repository)
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