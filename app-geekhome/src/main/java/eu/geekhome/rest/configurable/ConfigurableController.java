package eu.geekhome.rest.configurable;

import eu.geekhome.rest.PluginsCoordinator;
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

    private final PluginsCoordinator _pluginsCoordinator;
    private final ConfigurableDtoMapper _configurableDtoMapper;

    @Inject
    public ConfigurableController(PluginsCoordinator pluginsCoordinator, ConfigurableDtoMapper configurableDtoMapper) {
        _pluginsCoordinator = pluginsCoordinator;
        _configurableDtoMapper = configurableDtoMapper;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<ConfigurableDto> getConfigurables(@Context HttpServletRequest request) {
        return _pluginsCoordinator
                .getConfigurables()
                .stream()
                .map(_configurableDtoMapper::map)
                .collect(Collectors.toList());
    }
}
