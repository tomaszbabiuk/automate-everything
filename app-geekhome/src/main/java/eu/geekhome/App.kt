package eu.geekhome

import eu.geekhome.domain.automation.AutomationConductor
import eu.geekhome.domain.automation.blocks.BlockFactoriesCollector
import eu.geekhome.rest.*
import eu.geekhome.domain.events.NumberedEventsSink
import eu.geekhome.domain.extensibility.PluginsCoordinator
import eu.geekhome.domain.extensibility.SingletonExtensionsPluginsCoordinator
import eu.geekhome.domain.hardware.HardwareManager
import eu.geekhome.domain.langateway.LanGatewayResolver
import eu.geekhome.domain.mqtt.MqttBrokerService
import eu.geekhome.domain.heartbeat.Pulser
import eu.geekhome.langateway.JavaLanGatewayResolver
import eu.geekhome.pluginfeatures.mqtt.MoquetteBroker
import eu.geekhome.sqldelightplugin.SqlDelightRepository
import org.glassfish.jersey.server.ResourceConfig

open class App : ResourceConfig() {
    init {
        val liveEvents = NumberedEventsSink()
        val pulser = Pulser(liveEvents)
        val repository = SqlDelightRepository()
        val pluginsCoordinator: PluginsCoordinator = SingletonExtensionsPluginsCoordinator(liveEvents)
        pluginsCoordinator.loadPlugins()

        val hardwareManager = HardwareManager(pluginsCoordinator, liveEvents, repository)
        val blockFactoriesCoordinator = BlockFactoriesCollector(pluginsCoordinator, repository)
        val automationConductor = AutomationConductor(hardwareManager, blockFactoriesCoordinator, pluginsCoordinator,
            liveEvents, repository)

        //Dependency injection of REST controllers
        packages("eu.geekhome.rest")
        register(DependencyInjectionBinder(liveEvents, repository, pluginsCoordinator, hardwareManager,
            automationConductor, blockFactoriesCoordinator))
        register(GsonMessageBodyHandler())
        register(CORSFilter())
        register(ResourceNotFoundExceptionMapper())

        //dependency injection of Plugins
        val mqttBrokerService: MqttBrokerService = MoquetteBroker()
        val lanGatewayResolver: LanGatewayResolver = JavaLanGatewayResolver()
        pluginsCoordinator.injectPlugins(mqttBrokerService, lanGatewayResolver)

        //start
        pluginsCoordinator.startPlugins()
        hardwareManager.start()
        mqttBrokerService.start()
        pulser.start()
    }
}