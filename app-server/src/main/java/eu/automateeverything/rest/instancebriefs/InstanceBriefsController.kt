package eu.automateeverything.rest.instancebriefs

import eu.automateeverything.data.Repository
import eu.automateeverything.domain.extensibility.PluginsCoordinator
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Produces
import jakarta.ws.rs.PathParam
import eu.automateeverything.data.configurables.ConfigurableType
import eu.automateeverything.data.instances.InstanceBriefDto
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType

@Path("instancebriefs")
class InstanceBriefsController @Inject constructor(
    private val pluginsCoordinator: PluginsCoordinator,
    private val repository: Repository
) {

    @get:Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @get:GET
    val allInstancesBriefs: List<InstanceBriefDto>
        get() = repository
            .getAllInstanceBriefs()

    @GET
    @Path("/{configurableType}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getAllInstancesBriefsOfConfigurableType(@PathParam("configurableType") type: ConfigurableType?): List<InstanceBriefDto> {
        val classesOfConfigurableType: List<String> = pluginsCoordinator
            .configurables
            .map { configurable -> configurable.javaClass.name }
            .toList()

        return repository
            .getAllInstanceBriefs()
            .filter { instanceBriefDto: InstanceBriefDto -> classesOfConfigurableType.contains(instanceBriefDto.clazz) }
            .toList()
    }
}