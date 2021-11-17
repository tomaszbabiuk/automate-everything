package eu.automateeverything.rest.plugins

import eu.automateeverything.data.plugins.PluginDto
import eu.automateeverything.domain.extensibility.PluginsCoordinator
import eu.automateeverything.domain.ResourceNotFoundException
import org.pf4j.PluginWrapper
import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType

@Singleton
@Path("plugins")
class PluginsController @Inject constructor(
    private val pluginsCoordinator: PluginsCoordinator,
    private val pluginDtoMapper: PluginDtoMapper
) {

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    fun getPlugins(): List<PluginDto> {
        return pluginsCoordinator
            .plugins
            .map { pluginWrapper: PluginWrapper -> pluginDtoMapper.map(pluginWrapper) }
            .toList()
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    fun getPlugin(@PathParam("id") id: String?): PluginDto {
        if (id == null) {
            throw ResourceNotFoundException()
        }

        val pluginWrapper = pluginsCoordinator.getPluginWrapper(id)
            ?: throw ResourceNotFoundException()

        return pluginDtoMapper.map(pluginWrapper)
    }

    @PUT
    @Path("/{id}/enabled")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    fun updateEnableState(@PathParam("id") id: String?, enable: Boolean): PluginDto {
        if (id == null) {
            throw ResourceNotFoundException()
        }

        val pluginWrapper = (if (enable) pluginsCoordinator.enablePlugin(id) else pluginsCoordinator.disablePlugin(id))
            ?: throw ResourceNotFoundException()

        return pluginDtoMapper.map(pluginWrapper)
    }
}