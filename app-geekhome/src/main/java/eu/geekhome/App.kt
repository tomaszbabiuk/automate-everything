package eu.geekhome

import eu.geekhome.data.Repository
import eu.geekhome.domain.automation.AutomationConductor
import eu.geekhome.domain.automation.blocks.BlockFactoriesCollector
import eu.geekhome.domain.events.EventsSink
import eu.geekhome.rest.*
import eu.geekhome.domain.events.NumberedEventsSink
import eu.geekhome.domain.extensibility.PluginsCoordinator
import eu.geekhome.domain.extensibility.SingletonExtensionsPluginsCoordinator
import eu.geekhome.domain.firstrun.FileCheckingFirstRunService
import eu.geekhome.domain.firstrun.FirstRunService
import eu.geekhome.domain.hardware.HardwareManager
import eu.geekhome.domain.langateway.LanGatewayResolver
import eu.geekhome.domain.mqtt.MqttBrokerService
import eu.geekhome.domain.heartbeat.Pulsar
import eu.geekhome.domain.inbox.BroadcastingInbox
import eu.geekhome.domain.inbox.Inbox
import eu.geekhome.langateway.JavaLanGatewayResolver
import eu.geekhome.pluginfeatures.mqtt.MoquetteBroker
import eu.geekhome.sqldelightplugin.SqlDelightRepository
import org.glassfish.jersey.server.ResourceConfig

open class App : ResourceConfig() {

    //manual injection of common services
    private val firstRunService: FirstRunService = FileCheckingFirstRunService()
    private val eventsSink: EventsSink = NumberedEventsSink()
    private val repository: Repository = SqlDelightRepository()
    private val inbox: Inbox = BroadcastingInbox(eventsSink, repository)
    private val pluginsCoordinator: PluginsCoordinator = SingletonExtensionsPluginsCoordinator(eventsSink)
    private val hardwareManager = HardwareManager(pluginsCoordinator, eventsSink, inbox, repository)
    private val blockFactoriesCoordinator = BlockFactoriesCollector(pluginsCoordinator, repository)
    private val automationConductor = AutomationConductor(hardwareManager, blockFactoriesCoordinator, pluginsCoordinator,
        eventsSink, inbox, repository)
    private val pulsar = Pulsar(eventsSink, inbox, automationConductor)
    private val mqttBrokerService: MqttBrokerService = MoquetteBroker()
    private val lanGatewayResolver: LanGatewayResolver = JavaLanGatewayResolver()

    init {
        dependencyInjectionOfRest()

        preparePlugins()

        bootstrapProcedure()

        firstRunProcedure()
    }

    private fun firstRunProcedure() {
        if (firstRunService.isFirstRun()) {
            inbox.sendAppStarted()
            firstRunService.markFirstRunHappened()
        }
    }

    private fun bootstrapProcedure() {
        pluginsCoordinator.startPlugins()
        hardwareManager.start()
        mqttBrokerService.start()
        pulsar.start()
    }

    private fun preparePlugins() {
        pluginsCoordinator.loadPlugins()
        pluginsCoordinator.injectPlugins(mqttBrokerService, lanGatewayResolver)
    }

    private fun dependencyInjectionOfRest() {
        packages(REST_PACKAGE_NAME)
        register(
            DependencyInjectionBinder(
                eventsSink, repository, pluginsCoordinator, hardwareManager,
                automationConductor, blockFactoriesCoordinator, inbox
            )
        )
        register(GsonMessageBodyHandler())
        register(CORSFilter())
        register(ResourceNotFoundExceptionMapper())
    }

    companion object {
        const val REST_PACKAGE_NAME = "eu.geekhome.rest"
    }
}