package eu.geekhome.rest.configurable;

import com.geekhome.common.configurable.Configurable;
import eu.geekhome.rest.PluginsManager;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("configurables")
public class ConfigurableController {

    private PluginsManager _pluginsManager;
    private ConfigurableDtoMapper _configurableDtoMapper;

    @Inject
    public ConfigurableController(PluginsManager pluginsManager, ConfigurableDtoMapper configurableDtoMapper) {
        _pluginsManager = pluginsManager;
        _configurableDtoMapper = configurableDtoMapper;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<ConfigurableDto> getConfigurables(@Context HttpServletRequest request) {

        List<Configurable> allConfigurables = _pluginsManager
                .getConfigurables();

        return _pluginsManager
                .getConfigurables()
                .stream()
                .filter((x) -> x.getParent() == null)
                .map((x) -> _configurableDtoMapper.map(x, allConfigurables))
                .collect(Collectors.toList());
    }
}
