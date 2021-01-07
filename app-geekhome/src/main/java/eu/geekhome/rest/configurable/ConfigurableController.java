package eu.geekhome.rest.configurable;

import eu.geekhome.rest.PluginsManager;
import eu.geekhome.services.configurable.ConfigurableDto;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("configurables")
public class ConfigurableController {

    private final PluginsManager _pluginsManager;
    private final ConfigurableDtoMapper _configurableDtoMapper;

    @Inject
    public ConfigurableController(PluginsManager pluginsManager, ConfigurableDtoMapper configurableDtoMapper) {
        _pluginsManager = pluginsManager;
        _configurableDtoMapper = configurableDtoMapper;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<ConfigurableDto> getConfigurables(@Context HttpServletRequest request) {
        return _pluginsManager
                .getConfigurables()
                .stream()
                .map(_configurableDtoMapper::map)
                .collect(Collectors.toList());
    }
}
