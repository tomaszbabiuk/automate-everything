package eu.geekhome.rest;

import eu.geekhome.services.configurable.Configurable;
import eu.geekhome.services.repository.Repository;
import org.jvnet.hk2.annotations.Service;
import org.pf4j.PluginManager;
import org.pf4j.PluginStateListener;
import org.pf4j.PluginWrapper;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import java.util.List;

@Service
public class PluginsCoordinator extends HolderService<PluginManager> {

    public PluginsCoordinator(@Context Application app) throws Exception {
        super(app, PluginManager.class);
    }

    public List<PluginWrapper> getPlugins() {
        return getInstance().getPlugins();
    }

    public PluginWrapper getPlugin(String id) {
        return getInstance().getPlugin(id);
    }

    public PluginWrapper enablePlugin(String id) {
        getInstance().enablePlugin(id);
        getInstance().startPlugin(id);
        return getPlugin(id);
    }

    public PluginWrapper disablePlugin(String id) {
        getInstance().stopPlugin(id);
        getInstance().disablePlugin(id);
        return getPlugin(id);
    }

    public List<Configurable> getConfigurables() {
        return getInstance()
                .getExtensions(Configurable.class);
    }

    public List<Repository> getRepositories() {
        return getInstance()
                .getExtensions(Repository.class);
    }

    public void registerStateListener(PluginStateListener listener) {
        getInstance().addPluginStateListener(listener);
    }
}
