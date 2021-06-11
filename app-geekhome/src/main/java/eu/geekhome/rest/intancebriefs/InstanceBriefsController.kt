package eu.geekhome.rest.intancebriefs

import eu.geekhome.PluginsCoordinator
import javax.inject.Inject
import eu.geekhome.rest.PluginsCoordinatorHolderService
import javax.ws.rs.GET
import javax.ws.rs.Produces
import eu.geekhome.domain.repository.InstanceBriefDto
import javax.ws.rs.PathParam
import eu.geekhome.domain.configurable.ConfigurableType
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType

@Path("instancebriefs")
class InstanceBriefsController @Inject constructor(
    pluginsCoordinatorHolderService: PluginsCoordinatorHolderService) {

    private val pluginsCoordinator: PluginsCoordinator = pluginsCoordinatorHolderService.instance

    @get:Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @get:GET
    val allInstancesBriefs: List<InstanceBriefDto>
        get() = pluginsCoordinator
            .repository
            .getAllInstanceBriefs()

    @GET
    @Path("/{configurableType}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getAllInstancesBriefsOfConfigurableType(@PathParam("configurableType") type: ConfigurableType?): List<InstanceBriefDto> {
        val classesOfConfigurableType: List<String> = pluginsCoordinator
            .configurables
            .map { configurable -> configurable.javaClass.name }
            .toList()

        return pluginsCoordinator
            .repository
            .getAllInstanceBriefs()
            .filter { instanceBriefDto: InstanceBriefDto -> classesOfConfigurableType.contains(instanceBriefDto.clazz) }
            .toList()
    }
}