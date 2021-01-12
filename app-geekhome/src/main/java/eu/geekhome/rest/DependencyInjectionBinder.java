package eu.geekhome.rest;

import eu.geekhome.rest.configurable.ConfigurableDtoMapper;
import eu.geekhome.rest.configurable.FieldDefinitionDtoMapper;
import eu.geekhome.rest.hardware.HardwareAdapterDtoMapper;
import eu.geekhome.rest.plugins.PluginDtoMapper;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class DependencyInjectionBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(FieldDefinitionDtoMapper.class).to(FieldDefinitionDtoMapper.class).in(Singleton.class);
        bind(ConfigurableDtoMapper.class).to(ConfigurableDtoMapper.class).in(Singleton.class);
        bind(PluginDtoMapper.class).to(PluginDtoMapper.class).in(Singleton.class);
        bind(HardwareAdapterDtoMapper.class).to(HardwareAdapterDtoMapper.class).in(Singleton.class);
        bind(PluginsCoordinator.class).to(PluginsCoordinator.class).in(Singleton.class);
        bind(HardwareManagerHolderService.class).to(HardwareManagerHolderService.class).in(Singleton.class);
    }
}
