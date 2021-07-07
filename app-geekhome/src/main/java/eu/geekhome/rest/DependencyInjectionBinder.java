package eu.geekhome.rest;

import eu.geekhome.HardwareManager;
import eu.geekhome.PluginsCoordinator;
import eu.geekhome.automation.AutomationConductor;
import eu.geekhome.automation.BlocklyParser;
import eu.geekhome.automation.blocks.BlockFactoriesCollector;
import eu.geekhome.domain.events.EventsSink;
import eu.geekhome.domain.repository.Repository;
import eu.geekhome.heartbeat.HeartbeatDtoMapper;
import eu.geekhome.rest.automation.AutomationUnitDtoMapper;
import eu.geekhome.rest.automation.EvaluationResultDtoMapper;
import eu.geekhome.rest.automationhistory.AutomationHistoryDtoMapper;
import eu.geekhome.rest.configurable.ConfigurableDtoMapper;
import eu.geekhome.rest.field.FieldDefinitionDtoMapper;
import eu.geekhome.rest.hardware.HardwareAdapterDtoMapper;
import eu.geekhome.rest.hardware.NumberedHardwareEventToEventDtoMapper;
import eu.geekhome.rest.hardware.PortDtoMapper;
import eu.geekhome.rest.plugins.PluginDtoMapper;
import eu.geekhome.rest.settinggroup.SettingGroupDtoMapper;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class DependencyInjectionBinder extends AbstractBinder {

    private final EventsSink _eventsSink;
    private final Repository _repository;
    private final PluginsCoordinator _pluginsCoordinator;
    private final HardwareManager _hardwareManager;
    private final AutomationConductor _automationConductor;
    private final BlockFactoriesCollector _blockFactoriesCollector;

    public DependencyInjectionBinder(
            EventsSink eventsSink,
            Repository repository,
            PluginsCoordinator pluginsCoordinator,
            HardwareManager hardwareManager,
            AutomationConductor automationConductor,
            BlockFactoriesCollector blockFactoriesCollector
    ) {
        _eventsSink = eventsSink;
        _repository = repository;
        _pluginsCoordinator = pluginsCoordinator;
        _hardwareManager = hardwareManager;
        _automationConductor = automationConductor;
        _blockFactoriesCollector = blockFactoriesCollector;
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

        //factories for objects shared with the App
        bindFactory(new SingletonFactory<>(_hardwareManager)).to(HardwareManager.class).in(Singleton.class);
        bindFactory(new SingletonFactory<>(_automationConductor)).to(AutomationConductor.class).in(Singleton.class);
        bindFactory(new SingletonFactory<>(_pluginsCoordinator)).to(PluginsCoordinator.class).in(Singleton.class);
        bindFactory(new SingletonFactory<>(_repository)).to(Repository.class).in(Singleton.class);
        bindFactory(new SingletonFactory<>(_eventsSink)).to(EventsSink.class).in(Singleton.class);
        bindFactory(new SingletonFactory<>(_blockFactoriesCollector)).to(BlockFactoriesCollector.class).in(Singleton.class);

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
