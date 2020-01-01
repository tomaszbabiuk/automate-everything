package eu.geekhome.rest;

import eu.geekhome.plugins.PluginDto;
import eu.geekhome.plugins.PluginsManager;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public PluginDto[] getMessage() {
        return _pluginsManager.getPlugins();
    }
}