package eu.geekhome.rest;


import com.geekhome.common.configurable.Configurable;
import eu.geekhome.rest.configurable.ConfigurableDto;
import eu.geekhome.rest.configurable.ConfigurableDtoMapper;
import eu.geekhome.rest.plugins.PluginDto;
import eu.geekhome.rest.plugins.PluginDtoMapper;
import org.jvnet.hk2.annotations.Service;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginWrapper;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PluginsManager {

    private final DefaultPluginManager _pluginManager;

    @Inject
    private PluginDtoMapper _pluginDtoMapper;

    @Inject
    private ConfigurableDtoMapper _configurableDtoMapper;

    public PluginsManager() {
        _pluginManager = new DefaultPluginManager();
        _pluginManager.loadPlugins();
    }

    public List<PluginDto> getPlugins() {
        return _pluginManager
                .getPlugins()
                .stream()
                .map(_pluginDtoMapper::map)
                .collect(Collectors.toList());
    }


    public PluginDto getPlugin(String id) {
        PluginWrapper plugin = _pluginManager.getPlugin(id);
        if (plugin != null) {
            return _pluginDtoMapper.map(plugin);
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

    public List<ConfigurableDto> getConfigurables() {
        return _pluginManager
                .getExtensions(Configurable.class)
                .stream()
                .map(_configurableDtoMapper::map)
                .collect(Collectors.toList());
    }
}
