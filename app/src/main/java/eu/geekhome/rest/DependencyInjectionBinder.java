package eu.geekhome.rest;

import eu.geekhome.plugins.PluginsManager;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class DependencyInjectionBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(PluginsManager.class).to(PluginsManager.class).in(Singleton.class);
    }
}
