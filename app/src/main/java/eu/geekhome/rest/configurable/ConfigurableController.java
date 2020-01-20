package eu.geekhome.rest.configurable;

import eu.geekhome.rest.PluginsManager;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("configurables")
public class ConfigurableController {

    private PluginsManager _pluginsManager;

    @Inject
    public ConfigurableController(PluginsManager pluginsManager) {
        _pluginsManager = pluginsManager;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<ConfigurableDto> getConfigurables(@Context HttpServletRequest request) {
        return _pluginsManager.getConfigurables();
    }
}