package eu.geekhome.plugins;


import com.geekhome.common.configuration.DescriptiveName;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginState;
import org.pf4j.PluginWrapper;

public class PluginsManager {

    private final PluginDto[] _plugins;
    private final DefaultPluginManager _pluginManager;

    public PluginsManager() {
        _pluginManager = new DefaultPluginManager();
        _pluginManager.loadPlugins();
        _plugins = _pluginManager
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

    public PluginDto getPlugin(String id) {
        PluginWrapper plugin = _pluginManager.getPlugin(id);
        if (plugin != null) {
            return mapPluginWrapperToPluginDto(plugin);
        }

        return null;
    }

    public PluginDto enablePlugin(String id) {
        _pluginManager.enablePlugin(id);
        _pluginManager.startPlugin(id);
        return getPlugin(id);
    }

    public PluginDto disablePlugin(String id) {
        _pluginManager.stopPlugin(id);
        _pluginManager.disablePlugin(id);
        return getPlugin(id);
    }
}
