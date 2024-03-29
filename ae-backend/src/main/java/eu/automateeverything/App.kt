/*
 * Copyright (c) 2019-2023 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything

import eu.automateeverything.data.DataRepository
import eu.automateeverything.domain.automation.AutomationConductor
import eu.automateeverything.domain.automation.blocks.MasterBlockFactoriesCollector
import eu.automateeverything.domain.events.EventBus
import eu.automateeverything.domain.events.NumberedEventBus
import eu.automateeverything.domain.extensibility.ConfigurableRepository
import eu.automateeverything.domain.extensibility.InjectionRegistry
import eu.automateeverything.domain.extensibility.PluginsCoordinator
import eu.automateeverything.domain.extensibility.SingletonExtensionPluginsCoordinator
import eu.automateeverything.domain.firstrun.FileCheckingFirstRunService
import eu.automateeverything.domain.firstrun.FirstRunService
import eu.automateeverything.domain.hardware.HardwareManager
import eu.automateeverything.domain.hardware.PortFinder
import eu.automateeverything.domain.heartbeat.Pulsar
import eu.automateeverything.domain.inbox.BroadcastingInbox
import eu.automateeverything.domain.inbox.Inbox
import eu.automateeverything.domain.langateway.LanGatewayResolver
import eu.automateeverything.domain.mqtt.MqttBrokerService
import eu.automateeverything.interop.ByteArraySessionHandler
import eu.automateeverything.interop.SessionHandler
import eu.automateeverything.jsonrpc2.*
import eu.automateeverything.langateway.JavaLanGatewayResolver
import eu.automateeverything.mappers.*
import eu.automateeverything.pluginfeatures.mqtt.MoquetteBroker
import eu.automateeverything.rest.*
import eu.automateeverything.sqldelightplugin.SqlDelightDataRepository
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import org.glassfish.jersey.server.ResourceConfig
import org.slf4j.LoggerFactory

open class App : ResourceConfig() {

    private val logger = LoggerFactory.getLogger(App::class.java)

    @OptIn(ExperimentalSerializationApi::class)
    private val binaryFormat = Cbor { ignoreUnknownKeys = true }

    // manual injection of common services
    private val injectionRegistry: InjectionRegistry = InjectionRegistry()
    private val firstRunService: FirstRunService = FileCheckingFirstRunService()
    private val eventBus: EventBus = NumberedEventBus()
    private val dataRepository: DataRepository = SqlDelightDataRepository()
    private val inbox: Inbox = BroadcastingInbox(eventBus, dataRepository)
    private val pluginsCoordinator: PluginsCoordinator =
        SingletonExtensionPluginsCoordinator(eventBus, injectionRegistry, dataRepository)
    private val hardwareManager =
        HardwareManager(pluginsCoordinator, eventBus, inbox, dataRepository)
    private val blockFactoriesCoordinator =
        MasterBlockFactoriesCollector(pluginsCoordinator, dataRepository)
    private val automationConductor =
        AutomationConductor(
            hardwareManager,
            blockFactoriesCoordinator,
            pluginsCoordinator,
            eventBus,
            inbox,
            dataRepository
        )
    private val pulsar = Pulsar(eventBus, inbox, automationConductor)
    private val mqttBrokerService: MqttBrokerService = MoquetteBroker()
    private val lanGatewayResolver: LanGatewayResolver = JavaLanGatewayResolver()

    private val sessionHandler: SessionHandler<ByteArray, ByteArray> = buildJsonRpc2SessionHandler()

    @OptIn(ExperimentalSerializationApi::class)
    private fun buildJsonRpc2SessionHandler(): SessionHandler<ByteArray, ByteArray> {
        val portMapper = PortDtoMapper()
        val fieldDefinitionMapper = FieldDefinitionDtoMapper()
        val settingGroupMapper = SettingGroupDtoMapper(fieldDefinitionMapper)
        val pluginMapper = PluginDtoMapper(settingGroupMapper)
        val discoveryEventMapper = DiscoveryEventMapper()
        val evaluationResultMapper = EvaluationResultDtoMapper()
        val automationUnitMapper = AutomationUnitDtoMapper(evaluationResultMapper)
        val automationHistoryMapper = AutomationHistoryDtoMapper()
        val heartbeatMapper = HeartbeatDtoMapper()
        val inboxMessageMapper = InboxMessageDtoMapper()
        val descriptionsUpdateDtoMapper = DescriptionsUpdateDtoMapper()
        val configurablesDtoMapper = ConfigurableDtoMapper(fieldDefinitionMapper)

        val eventsMapper =
            LiveEventsMapper(
                portMapper,
                pluginMapper,
                discoveryEventMapper,
                automationUnitMapper,
                automationHistoryMapper,
                heartbeatMapper,
                inboxMessageMapper,
                descriptionsUpdateDtoMapper
            )

        val eventsSubscriptionBuilder =
            EventsSubscriptionBuilder(eventBus, eventsMapper, binaryFormat)

        val methodHandlers =
            listOf(
                InstancesMethodHandler(dataRepository),
                MessagesMethodHandler(dataRepository),
                IconsMethodHandler(dataRepository),
                TagsMethodHandler(dataRepository),
                VersionsMethodHandler(dataRepository),
                ConfigurablesMethodHandler(pluginsCoordinator, configurablesDtoMapper),
                AutomationUnitsMethodHandler(automationConductor, automationUnitMapper),
                SubscribeToEventsMethodHandler(eventsSubscriptionBuilder)
            )

        return ByteArraySessionHandler(methodHandlers, binaryFormat)
    }

    init {
        fillInjectionRegistry()

        dependencyInjectionOfRest()

        loadPlugins()

        bootstrapProcedure()

        firstRunProcedure()

        waitUntilDiscoveryFinishes()
    }

    private fun waitUntilDiscoveryFinishes() {
        val initialDiscoveryScope = CoroutineScope(Dispatchers.IO)
        initialDiscoveryScope.launch {
            while (isActive) {
                delay(5000)
                val nonOperatingAdaptersCount = hardwareManager.countNonOperatingAdapters()
                logger.info("Waiting for discovery to finish $nonOperatingAdaptersCount")
                if (nonOperatingAdaptersCount == 0) {
                    automationConductor.enable()
                    cancel("All adapters are not initialized")
                }
            }
        }
    }

    private fun fillInjectionRegistry() {
        injectionRegistry.put(PortFinder::class.java, hardwareManager)
        injectionRegistry.put(EventBus::class.java, eventBus)
        injectionRegistry.put(Inbox::class.java, inbox)
        injectionRegistry.put(MqttBrokerService::class.java, mqttBrokerService)
        injectionRegistry.put(LanGatewayResolver::class.java, lanGatewayResolver)
        injectionRegistry.put(DataRepository::class.java, dataRepository)
        injectionRegistry.put(ByteArraySessionHandler::class.java, sessionHandler)
        injectionRegistry.put(ConfigurableRepository::class.java, pluginsCoordinator)
    }

    private fun firstRunProcedure() {
        if (firstRunService.isFirstRun()) {
            inbox.sendMessage(R.inbox_message_welcome_subject, R.inbox_message_welcome_body)
            firstRunService.markFirstRunHappened()
        }
    }

    private fun bootstrapProcedure() {
        mqttBrokerService.start()
        pluginsCoordinator.startPlugins()
        hardwareManager.start(null)
        pulsar.start(null)
    }

    private fun loadPlugins() {
        pluginsCoordinator.loadPlugins()
    }

    private fun dependencyInjectionOfRest() {
        packages(REST_PACKAGE_NAME)
        register(
            DependencyInjectionBinder(
                eventBus,
                dataRepository,
                pluginsCoordinator,
                hardwareManager,
                automationConductor,
                blockFactoriesCoordinator,
                inbox
            )
        )
        register(GsonMessageBodyHandler())
        register(CORSFilter())
        register(ResourceNotFoundExceptionMapper())
        register(ServerExceptionMapper())
    }

    companion object {
        const val REST_PACKAGE_NAME = "eu.automateeverything.rest"
    }
}
