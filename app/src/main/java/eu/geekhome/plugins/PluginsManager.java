package eu.geekhome.plugins;


import com.geekhome.common.configuration.DescriptiveName;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.PluginState;
import org.pf4j.PluginWrapper;

public class PluginsManager {

    private final PluginDto[] _plugins;

    public PluginsManager() {
        PluginManager pluginManager = new DefaultPluginManager();
        pluginManager.loadPlugins();
        _plugins = pluginManager
                .getPlugins()
                .stream()
                .map(this::mapPluginWrapperToPluginDto)
                .toArray(PluginDto[]::new);
    }

    private PluginDto mapPluginWrapperToPluginDto(PluginWrapper plugin) {
        String name = plugin.getPluginId();
        String description = plugin.getDescriptor().getPluginDescription();
        String version = plugin.getDescriptor().getVersion();
        String provider = plugin.getDescriptor().getProvider();
        DescriptiveName dn = new DescriptiveName(description, name, version + " by " + provider);
        return new PluginDto(dn, plugin.getPluginState() == PluginState.STARTED);
    }

    public PluginDto[] getPlugins() {
        return _plugins;
    }
}
