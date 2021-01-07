package eu.geekhome.rest;

import com.geekhome.common.hardwaremanager.IHardwareManagerAdapterFactory;
import eu.geekhome.services.configurable.Configurable;
import eu.geekhome.services.hardware.HardwarePlugin;
import eu.geekhome.services.repository.Repository;
import org.jvnet.hk2.annotations.Service;
import org.pf4j.*;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PluginsManager {

    private PluginManager _pluginManager;

    public PluginsManager(@Context Application app) throws Exception {
        for (Object singleton : app.getSingletons()) {
            if (singleton instanceof PluginsBootstrap) {
                _pluginManager = ((PluginsBootstrap) singleton).getPluginManager();
                break;
            }
        }
        if (_pluginManager == null) {
            throw new Exception("There's no PluginsBootstrap in the app singletons container");
        }
    }

    public List<PluginWrapper> getPlugins() {
        return _pluginManager.getPlugins();
    }

    public PluginWrapper getPlugin(String id) {
        return _pluginManager.getPlugin(id);
    }

    public PluginWrapper enablePlugin(String id) {
        _pluginManager.enablePlugin(id);
        _pluginManager.startPlugin(id);
        return getPlugin(id);
    }

    public PluginWrapper disablePlugin(String id) {
        _pluginManager.stopPlugin(id);
        _pluginManager.disablePlugin(id);
        return getPlugin(id);
    }

    public List<Configurable> getConfigurables() {
        return _pluginManager
                .getExtensions(Configurable.class);
    }

    public List<Repository> getRepositories() {
        return _pluginManager
                .getExtensions(Repository.class);
    }

    public List<IHardwareManagerAdapterFactory> getHardwareManagerAdapterFactories() {
        return _pluginManager
                .getPlugins()
                .stream()
                .filter(plugin -> plugin.getPluginState() == PluginState.STARTED)
                .filter(plugin -> plugin.getPlugin() instanceof HardwarePlugin)
                .map(plugin -> ((HardwarePlugin) plugin.getPlugin()).getFactory())
                .collect(Collectors.toList());
    }
}
