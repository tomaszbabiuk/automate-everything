package eu.geekhome

import eu.geekhome.data.Repository
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
import eu.geekhome.domain.inbox.Inbox
import eu.geekhome.langateway.JavaLanGatewayResolver
import eu.geekhome.pluginfeatures.mqtt.MoquetteBroker
import eu.geekhome.sqldelightplugin.SqlDelightRepository
import org.glassfish.jersey.server.ResourceConfig

open class App : ResourceConfig() {
    init {
        //manual injection of common service
        val eventsSink = NumberedEventsSink()
        val repository : Repository = SqlDelightRepository()
        val inbox = Inbox(eventsSink, repository)
        val pulser = Pulser(eventsSink)
        val pluginsCoordinator: PluginsCoordinator = SingletonExtensionsPluginsCoordinator(eventsSink)
        pluginsCoordinator.loadPlugins()

        val hardwareManager = HardwareManager(pluginsCoordinator, eventsSink, repository)
        val blockFactoriesCoordinator = BlockFactoriesCollector(pluginsCoordinator, repository)
        val automationConductor = AutomationConductor(hardwareManager, blockFactoriesCoordinator, pluginsCoordinator,
            eventsSink, repository)

        //Dependency injection of REST controllers
        packages("eu.geekhome.rest")
        register(DependencyInjectionBinder(eventsSink, repository, pluginsCoordinator, hardwareManager,
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

        inbox.broadcastAppStarted()
    }
}