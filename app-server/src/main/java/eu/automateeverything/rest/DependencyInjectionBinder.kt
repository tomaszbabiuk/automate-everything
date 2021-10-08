package eu.automateeverything.rest

import eu.automateeverything.data.Repository
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.extensibility.PluginsCoordinator
import eu.automateeverything.domain.hardware.HardwareManager
import eu.automateeverything.domain.automation.AutomationConductor
import eu.automateeverything.domain.automation.blocks.BlockFactoriesCollector
import eu.automateeverything.domain.inbox.Inbox
import eu.automateeverything.rest.fields.FieldDefinitionDtoMapper
import jakarta.inject.Singleton
import eu.automateeverything.rest.configurables.ConfigurableDtoMapper
import eu.automateeverything.rest.plugins.PluginDtoMapper
import eu.automateeverything.rest.hardware.HardwareAdapterDtoMapper
import eu.automateeverything.rest.automation.AutomationUnitDtoMapper
import eu.automateeverything.rest.automation.EvaluationResultDtoMapper
import eu.automateeverything.rest.hardware.PortDtoMapper
import eu.automateeverything.rest.hardware.NumberedHardwareEventToEventDtoMapper
import eu.automateeverything.rest.automationhistory.AutomationHistoryDtoMapper
import eu.automateeverything.rest.settinggroup.SettingGroupDtoMapper
import eu.automateeverything.rest.live.HeartbeatDtoMapper
import eu.automateeverything.rest.inbox.InboxMessageDtoMapper
import eu.automateeverything.domain.automation.BlocklyParser
import org.glassfish.hk2.api.Factory
import org.glassfish.hk2.utilities.binding.AbstractBinder

class DependencyInjectionBinder(
    private val eventsSink: EventsSink,
    private val repository: Repository,
    private val pluginsCoordinator: PluginsCoordinator,
    private val hardwareManager: HardwareManager,
    private val automationConductor: AutomationConductor,
    private val blockFactoriesCollector: BlockFactoriesCollector,
    private val inbox: Inbox
) : AbstractBinder() {

    override fun configure() {
        //mappers
        bind(FieldDefinitionDtoMapper::class.java)
            .to(FieldDefinitionDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(ConfigurableDtoMapper::class.java)
            .to(ConfigurableDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(PluginDtoMapper::class.java)
            .to(PluginDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(HardwareAdapterDtoMapper::class.java)
            .to(HardwareAdapterDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(AutomationUnitDtoMapper::class.java)
            .to(AutomationUnitDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(EvaluationResultDtoMapper::class.java)
            .to(EvaluationResultDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(PortDtoMapper::class.java)
            .to(PortDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(NumberedHardwareEventToEventDtoMapper::class.java)
            .to(NumberedHardwareEventToEventDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(AutomationHistoryDtoMapper::class.java)
            .to(AutomationHistoryDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(SettingGroupDtoMapper::class.java)
            .to(SettingGroupDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(HeartbeatDtoMapper::class.java)
            .to(HeartbeatDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(InboxMessageDtoMapper::class.java)
            .to(InboxMessageDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(BlocklyParser::class.java)
            .to(BlocklyParser::class.java)
            .`in`(Singleton::class.java)

        //factories for objects shared with the App
        bindFactory(SingletonFactory(hardwareManager))
            .to(HardwareManager::class.java)
            .`in`(Singleton::class.java)

        bindFactory(SingletonFactory(automationConductor))
            .to(AutomationConductor::class.java)
            .`in`(Singleton::class.java)

        bindFactory(SingletonFactory(pluginsCoordinator))
            .to(PluginsCoordinator::class.java)
            .`in`(Singleton::class.java)

        bindFactory(SingletonFactory(repository))
            .to(Repository::class.java)
            .`in`(Singleton::class.java)

        bindFactory(SingletonFactory(eventsSink))
            .to(EventsSink::class.java)
            .`in`(Singleton::class.java)

        bindFactory(SingletonFactory(blockFactoriesCollector))
            .to(BlockFactoriesCollector::class.java)
            .`in`(Singleton::class.java)

        bindFactory(SingletonFactory(inbox))
            .to(Inbox::class.java)
            .`in`(Singleton::class.java)

    }

    internal class SingletonFactory<T>(private val instance: T) : Factory<T> {
        override fun provide(): T {
            return instance
        }

        override fun dispose(instance: T) {}
    }
}