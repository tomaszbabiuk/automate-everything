package eu.geekhome.rest;

import eu.geekhome.rest.configurable.ConfigurableDtoMapper;
import eu.geekhome.rest.configurable.FieldDtoMapper;
import eu.geekhome.rest.plugins.PluginDtoMapper;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class DependencyInjectionBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(FieldDtoMapper.class).to(FieldDtoMapper.class).in(Singleton.class);
        bind(ConfigurableDtoMapper.class).to(ConfigurableDtoMapper.class).in(Singleton.class);
        bind(PluginDtoMapper.class).to(PluginDtoMapper.class).in(Singleton.class);
        bind(PluginsManager.class).to(PluginsManager.class).in(Singleton.class);
    }
}
