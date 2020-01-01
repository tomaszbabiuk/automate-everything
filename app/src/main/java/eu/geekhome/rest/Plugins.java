package eu.geekhome.rest;

import eu.geekhome.plugins.PluginDto;
import eu.geekhome.plugins.PluginsManager;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("plugins")
public class Plugins {

    private PluginsManager _pluginsManager;

    @Inject
    public Plugins(PluginsManager pluginsManager) {
        _pluginsManager = pluginsManager;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PluginDto[] getPlugins() {
        return _pluginsManager.getPlugins();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PluginDto getPlugin(@PathParam("id") String id) {
        return _pluginsManager.getPlugin(id);
    }

    @PUT
    @Path("/{id}/enabled")
    @Produces(MediaType.APPLICATION_JSON)
    public PluginDto updateEnableState(@PathParam("id") String id, boolean enable) {
        if (enable) {
            return _pluginsManager.enablePlugin(id);
        } else {
            return _pluginsManager.disablePlugin(id);
        }
    }
}