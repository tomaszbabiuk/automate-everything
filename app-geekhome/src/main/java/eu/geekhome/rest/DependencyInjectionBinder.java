package eu.geekhome.rest;

import eu.geekhome.automation.BlocklyParser;
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
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class DependencyInjectionBinder extends AbstractBinder {

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

        //holders
        bind(PluginsCoordinatorHolderService.class).to(PluginsCoordinatorHolderService.class).in(Singleton.class);
        bind(HardwareManagerHolderService.class).to(HardwareManagerHolderService.class).in(Singleton.class);
        bind(EventsSinkHolderService.class).to(EventsSinkHolderService.class).in(Singleton.class);
        bind(AutomationConductorHolderService.class).to(AutomationConductorHolderService.class).in(Singleton.class);
        bind(RepositoryHolderService.class).to(RepositoryHolderService.class).in(Singleton.class);

        //blocks
        bind(BlocklyParser.class).to(BlocklyParser.class).in(Singleton.class);
        bind(BlockFactoriesCollectorHolderService.class).to(BlockFactoriesCollectorHolderService.class).in(Singleton.class);
    }
}
