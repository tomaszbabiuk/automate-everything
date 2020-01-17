package eu.geekhome.plugins;


import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.localization.Language;
import org.pf4j.DefaultPluginManager;
import org.pf4j.Plugin;
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

    private PluginDto mapPluginWrapperToPluginDto(PluginWrapper pluginWrapper) {
        Plugin plugin = pluginWrapper.getPlugin();
        String uniqueId = pluginWrapper.getPluginId();

        DescriptiveName dn;
        if (plugin instanceof PluginMetadata) {
            PluginMetadata metadata = (PluginMetadata)plugin;
            String name = metadata.getName().getValue(Language.EN);
            String description = metadata.getDescription().getValue(Language.EN);
            dn = new DescriptiveName(name, uniqueId, description);
        } else {
            String version = pluginWrapper.getDescriptor().getVersion();
            String provider = pluginWrapper.getDescriptor().getProvider();
            dn = new DescriptiveName(uniqueId, uniqueId, version + " by " + provider);
        }

        return new PluginDto(dn, pluginWrapper.getPluginState() == PluginState.STARTED);
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
