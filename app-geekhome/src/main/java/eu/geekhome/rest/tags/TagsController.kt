package eu.geekhome.rest.tags

import eu.geekhome.PluginsCoordinator
import javax.inject.Inject
import eu.geekhome.rest.PluginsCoordinatorHolderService
import eu.geekhome.domain.repository.TagDto
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("tags")
class TagsController @Inject constructor(pluginsCoordinatorHolderService: PluginsCoordinatorHolderService) {

    private val pluginsCoordinator: PluginsCoordinator = pluginsCoordinatorHolderService.instance

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun postTag(tagDto: TagDto?): Long {
        return pluginsCoordinator
            .repository
            .saveTag(tagDto!!)
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun putTag(tagDto: TagDto?) {
        pluginsCoordinator
            .repository
            .updateTag(tagDto!!)
    }

    @get:Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @get:GET
    val allTags: List<TagDto>
        get() = pluginsCoordinator
                    .repository
                    .getAllTags()

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun deleteTag(@PathParam("id") id: Long) {
        pluginsCoordinator
            .repository
            .deleteTag(id)
    }
}