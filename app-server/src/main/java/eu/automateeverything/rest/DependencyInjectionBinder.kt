package eu.automateeverything.rest;

import eu.automateeverything.data.Repository;
import eu.automateeverything.domain.hardware.HardwareManager;
import eu.automateeverything.domain.extensibility.PluginsCoordinator;
import eu.automateeverything.domain.automation.AutomationConductor;
import eu.automateeverything.domain.automation.BlocklyParser;
import eu.automateeverything.domain.automation.blocks.BlockFactoriesCollector;
import eu.automateeverything.domain.events.EventsSink;
import eu.automateeverything.domain.inbox.Inbox;
import eu.automateeverything.rest.inbox.InboxMessageDtoMapper;
import eu.automateeverything.rest.live.HeartbeatDtoMapper;
import eu.automateeverything.rest.automation.AutomationUnitDtoMapper;
import eu.automateeverything.rest.automation.EvaluationResultDtoMapper;
import eu.automateeverything.rest.automationhistory.AutomationHistoryDtoMapper;
import eu.automateeverything.rest.configurables.ConfigurableDtoMapper;
import eu.automateeverything.rest.fields.FieldDefinitionDtoMapper;
import eu.automateeverything.rest.hardware.HardwareAdapterDtoMapper;
import eu.automateeverything.rest.hardware.NumberedHardwareEventToEventDtoMapper;
import eu.automateeverything.rest.hardware.PortDtoMapper;
import eu.automateeverything.rest.plugins.PluginDtoMapper;
import eu.automateeverything.rest.settinggroup.SettingGroupDtoMapper;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import jakarta.inject.Singleton;

public class DependencyInjectionBinder extends AbstractBinder {

    private final EventsSink _eventsSink;
    private final Repository _repository;
    private final PluginsCoordinator _pluginsCoordinator;
    private final HardwareManager _hardwareManager;
    private final AutomationConductor _automationConductor;
    private final BlockFactoriesCollector _blockFactoriesCollector;
    private Inbox _inbox;

    public DependencyInjectionBinder(
            EventsSink eventsSink,
            Repository repository,
            PluginsCoordinator pluginsCoordinator,
            HardwareManager hardwareManager,
            AutomationConductor automationConductor,
            BlockFactoriesCollector blockFactoriesCollector,
            Inbox inbox
    ) {
        _eventsSink = eventsSink;
        _repository = repository;
        _pluginsCoordinator = pluginsCoordinator;
        _hardwareManager = hardwareManager;
        _automationConductor = automationConductor;
        _blockFactoriesCollector = blockFactoriesCollector;
        _inbox = inbox;
    }

    @Override
    protected void configure() {
        //mappers
        bind(FieldDefinitionDtoMapper.class).to(FieldDefinitionDtoMapper.class).in(Singleton.class);
        bind(ConfigurableDtoMapper.class).to(ConfigurableDtoMapper.class).in(Singleton.class);
        bind(PluginDtoMapper.class).to(PluginDtoMapper.class).in(Singleton.class);
        bind(HardwareAdapterDtoMapper.class).to(HardwareAdapterDtoMapper.class).in(Singleton.class);
        bind(AutomationUnitDtoMapper.class).to(AutomationUnitDtoMapper.class).in(Singleton.class);
        bind(EvaluationResultDtoMapper.class).to(EvaluationResultDtoMapper.class).in(Singleton.class);
        bind(PortDtoMapper.class).to(PortDtoMapper.class).in(Singleton.class);
        bind(NumberedHardwareEventToEventDtoMapper.class).to(NumberedHardwareEventToEventDtoMapper.class).in(Singleton.class);
        bind(AutomationHistoryDtoMapper.class).to(AutomationHistoryDtoMapper.class).in(Singleton.class);
        bind(SettingGroupDtoMapper.class).to(SettingGroupDtoMapper.class).in(Singleton.class);
        bind(HeartbeatDtoMapper.class).to(HeartbeatDtoMapper.class).in(Singleton.class);
        bind(InboxMessageDtoMapper.class).to(InboxMessageDtoMapper.class).in(Singleton.class);

        //factories for objects shared with the App
        bindFactory(new SingletonFactory<>(_hardwareManager)).to(HardwareManager.class).in(Singleton.class);
        bindFactory(new SingletonFactory<>(_automationConductor)).to(AutomationConductor.class).in(Singleton.class);
        bindFactory(new SingletonFactory<>(_pluginsCoordinator)).to(PluginsCoordinator.class).in(Singleton.class);
        bindFactory(new SingletonFactory<>(_repository)).to(Repository.class).in(Singleton.class);
        bindFactory(new SingletonFactory<>(_eventsSink)).to(EventsSink.class).in(Singleton.class);
        bindFactory(new SingletonFactory<>(_blockFactoriesCollector)).to(BlockFactoriesCollector.class).in(Singleton.class);
        bindFactory(new SingletonFactory<>(_inbox)).to(Inbox.class).in(Singleton.class);

        //blocks
        bind(BlocklyParser.class).to(BlocklyParser.class).in(Singleton.class);
    }

    static class SingletonFactory<T> implements Factory<T> {
        private final T instance;

        public SingletonFactory(T instance) {
            this.instance = instance;
        }

        @Override
        public T provide() {
            return instance;
        }

        @Override
        public void dispose(T instance) {
        }
    }
}
