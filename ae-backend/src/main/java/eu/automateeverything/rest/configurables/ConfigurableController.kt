package eu.automateeverything.rest.configurables

import eu.automateeverything.domain.extensibility.PluginsCoordinator
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Produces
import eu.automateeverything.data.configurables.ConfigurableDto
import jakarta.servlet.http.HttpServletRequest
import java.util.stream.Collectors
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType

@Path("configurables")
class ConfigurableController @Inject constructor(
    private val pluginsCoordinator: PluginsCoordinator,
    private val configurableDtoMapper: ConfigurableDtoMapper
) {
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getConfigurables(): List<ConfigurableDto> {
        return pluginsCoordinator
            .configurables
            .stream()
            .map { configurableDtoMapper.map(it) }
            .collect(Collectors.toList())
    }
}