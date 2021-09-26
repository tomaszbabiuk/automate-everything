package eu.automateeverything.rest.instancebriefs

import eu.automateeverything.data.Repository
import eu.automateeverything.domain.extensibility.PluginsCoordinator
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.PathParam
import eu.automateeverything.data.configurables.ConfigurableType
import eu.automateeverything.data.instances.InstanceBriefDto
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType

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