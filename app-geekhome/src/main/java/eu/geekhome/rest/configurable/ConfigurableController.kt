package eu.geekhome.rest.configurable

import eu.geekhome.domain.extensibility.PluginsCoordinator
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.servlet.http.HttpServletRequest
import eu.geekhome.domain.configurable.ConfigurableDto
import java.util.stream.Collectors
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType

@Path("configurables")
class ConfigurableController @Inject constructor(
    private val pluginsCoordinator: PluginsCoordinator,
    private val configurableDtoMapper: ConfigurableDtoMapper
) {
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getConfigurables(@Context request: HttpServletRequest?): List<ConfigurableDto> {
        return pluginsCoordinator
            .configurables
            .stream()
            .map { configurableDtoMapper.map(it) }
            .collect(Collectors.toList())
    }
}