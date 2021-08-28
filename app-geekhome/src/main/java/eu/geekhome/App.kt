package eu.geekhome

import eu.geekhome.data.Repository
import eu.geekhome.domain.automation.AutomationConductor
import eu.geekhome.domain.automation.BroadcastingStateChangeReporter
import eu.geekhome.domain.automation.StateChangeReporter
import eu.geekhome.domain.automation.blocks.BlockFactoriesCollector
import eu.geekhome.domain.events.EventsSink
import eu.geekhome.rest.*
import eu.geekhome.domain.events.NumberedEventsSink
import eu.geekhome.domain.extensibility.PluginsCoordinator
import eu.geekhome.domain.extensibility.SingletonExtensionPluginsCoordinator
import eu.geekhome.domain.firstrun.FileCheckingFirstRunService
import eu.geekhome.domain.firstrun.FirstRunService
import eu.geekhome.domain.hardware.HardwareManager
import eu.geekhome.domain.hardware.PortFinder
import eu.geekhome.domain.langateway.LanGatewayResolver
import eu.geekhome.domain.mqtt.MqttBrokerService
import eu.geekhome.domain.heartbeat.Pulsar
import eu.geekhome.domain.inbox.BroadcastingInbox
import eu.geekhome.domain.inbox.Inbox
import eu.geekhome.domain.extensibility.InjectionRegistry
import eu.geekhome.langateway.JavaLanGatewayResolver
import eu.geekhome.pluginfeatures.mqtt.MoquetteBroker
import eu.geekhome.sqldelightplugin.SqlDelightRepository
import org.glassfish.jersey.server.ResourceConfig

open class App : ResourceConfig() {

    //manual injection of common services
    private val injectionRegistry: InjectionRegistry = InjectionRegistry()
    private val firstRunService: FirstRunService = FileCheckingFirstRunService()
    private val eventsSink: EventsSink = NumberedEventsSink()
    private val repository: Repository = SqlDelightRepository()
    private val inbox: Inbox = BroadcastingInbox(eventsSink, repository)
    private val pluginsCoordinator: PluginsCoordinator = SingletonExtensionPluginsCoordinator(eventsSink, injectionRegistry)
    private val hardwareManager = HardwareManager(pluginsCoordinator, eventsSink, inbox, repository)
    private val blockFactoriesCoordinator = BlockFactoriesCollector(pluginsCoordinator, repository)
    private val stateChangeReporter: StateChangeReporter = BroadcastingStateChangeReporter(eventsSink)
    private val automationConductor = AutomationConductor(hardwareManager, blockFactoriesCoordinator, pluginsCoordinator,
        eventsSink, inbox, repository, stateChangeReporter)
    private val pulsar = Pulsar(eventsSink, inbox, automationConductor)
    private val mqttBrokerService: MqttBrokerService = MoquetteBroker()
    private val lanGatewayResolver: LanGatewayResolver = JavaLanGatewayResolver()

    init {
        fillInjectionRegistry()

        dependencyInjectionOfRest()

        loadPlugins()

        bootstrapProcedure()

        firstRunProcedure()
    }

    private fun fillInjectionRegistry() {
        injectionRegistry.put(PortFinder::class.java, hardwareManager)
        injectionRegistry.put(EventsSink::class.java, eventsSink)
        injectionRegistry.put(Inbox::class.java, inbox)
        injectionRegistry.put(StateChangeReporter::class.java, stateChangeReporter)
        injectionRegistry.put(MqttBrokerService::class.java, mqttBrokerService)
        injectionRegistry.put(LanGatewayResolver::class.java, lanGatewayResolver)
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

    private fun loadPlugins() {
        pluginsCoordinator.loadPlugins()
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