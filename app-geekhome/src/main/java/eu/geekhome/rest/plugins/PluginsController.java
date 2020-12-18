package eu.geekhome.rest.plugins;

import eu.geekhome.rest.PluginsManager;
import org.pf4j.PluginWrapper;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("plugins")
public class PluginsController {

    private final PluginsManager _pluginsManager;
    private final PluginDtoMapper _pluginDtoMapper;

    @Inject
    public PluginsController(PluginsManager pluginsManager, PluginDtoMapper pluginDtoMapper) {
        _pluginsManager = pluginsManager;
        _pluginDtoMapper = pluginDtoMapper;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public List<PluginDto> getPlugins(@Context HttpServletRequest request) {
        return _pluginsManager
                .getPlugins()
                .stream()
                .map(_pluginDtoMapper::map)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public PluginDto getPlugin(@PathParam("id") String id) {
        return _pluginDtoMapper.map(_pluginsManager.getPlugin(id));
    }

    @PUT
    @Path("/{id}/enabled")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public PluginDto updateEnableState(@PathParam("id") String id, boolean enable) {
        PluginWrapper pluginWrapper = enable ?
                _pluginsManager.enablePlugin(id) :
                _pluginsManager.disablePlugin(id);

        return _pluginDtoMapper.map(pluginWrapper);
    }
}