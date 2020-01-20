package eu.geekhome.rest.plugins;

import eu.geekhome.rest.PluginsManager;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("plugins")
public class PluginsController {

    private PluginsManager _pluginsManager;

    @Inject
    public PluginsController(PluginsManager pluginsManager) {
        _pluginsManager = pluginsManager;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<PluginDto> getPlugins(@Context HttpServletRequest request) {
        return _pluginsManager.getPlugins();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public PluginDto getPlugin(@PathParam("id") String id) {
        return _pluginsManager.getPlugin(id);
    }

    @PUT
    @Path("/{id}/enabled")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public PluginDto updateEnableState(@PathParam("id") String id, boolean enable) {
        if (enable) {
            return _pluginsManager.enablePlugin(id);
        } else {
            return _pluginsManager.disablePlugin(id);
        }
    }
}