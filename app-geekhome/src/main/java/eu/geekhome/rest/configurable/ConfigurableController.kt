package eu.geekhome.rest.configurable

import javax.inject.Inject
import eu.geekhome.rest.PluginsCoordinator
import eu.geekhome.services.configurable.Configurable
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.servlet.http.HttpServletRequest
import eu.geekhome.services.configurable.ConfigurableDto
import java.util.stream.Collectors
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType

@Path("configurables")
class ConfigurableController @Inject constructor(
    private val _pluginsCoordinator: PluginsCoordinator,
    private val _configurableDtoMapper: ConfigurableDtoMapper
) {
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getConfigurables(@Context request: HttpServletRequest?): List<ConfigurableDto> {
        return _pluginsCoordinator
            .configurables
            .stream()
            .map { configurable: Configurable? ->
                _configurableDtoMapper.map(
                    configurable!!
                )
            }
            .collect(Collectors.toList())
    }
}