package eu.geekhome;

import eu.geekhome.rest.CORSFilter;
import eu.geekhome.rest.DependencyInjectionBinder;
import eu.geekhome.rest.GsonMessageBodyHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.pf4j.ExtensionFactory;
import org.pf4j.JarPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.SingletonExtensionFactory;

@SuppressWarnings("unused")
public class App extends ResourceConfig {
    public App() {
        PluginManager pluginManager = buildPluginManager();
        startPlugins(pluginManager);

        HardwareManager hardwareManager = new HardwareManager(pluginManager);

        packages("eu.geekhome.rest");
        register(new DependencyInjectionBinder());
        register(new GsonMessageBodyHandler());
        register(new CORSFilter());
        register(pluginManager);
        register(hardwareManager);
    }

    private void startPlugins(PluginManager pluginManager) {
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
    }

    private PluginManager buildPluginManager() {
        return new JarPluginManager() {
            @Override
            protected ExtensionFactory createExtensionFactory() {
                return new SingletonExtensionFactory();
            }
        };
    }
}