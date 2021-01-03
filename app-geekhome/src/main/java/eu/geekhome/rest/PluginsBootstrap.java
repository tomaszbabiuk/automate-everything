package eu.geekhome.rest;


import org.pf4j.ExtensionFactory;
import org.pf4j.JarPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.SingletonExtensionFactory;

import javax.inject.Singleton;

@Singleton
public class PluginsBootstrap {

    private final PluginManager _pluginManager;

    public PluginManager getPluginManager() {
        return _pluginManager;
    }

    public PluginsBootstrap() {
        _pluginManager = new JarPluginManager() {
            @Override
            protected ExtensionFactory createExtensionFactory() {
                return new SingletonExtensionFactory();
            }
        };
        _pluginManager.loadPlugins();
        _pluginManager.startPlugins();
    }
}
