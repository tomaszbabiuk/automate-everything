package eu.geekhome.rest.icons

import eu.geekhome.PluginsCoordinator
import javax.inject.Inject
import eu.geekhome.rest.PluginsCoordinatorHolderService
import eu.geekhome.domain.repository.IconDto
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("icons")
class IconController @Inject constructor(pluginsCoordinatorHolderService: PluginsCoordinatorHolderService) {
    private val pluginsCoordinator: PluginsCoordinator = pluginsCoordinatorHolderService.instance

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun postIcon(iconDto: IconDto?): Long {
        return pluginsCoordinator
                    .repository
                    .saveIcon(iconDto!!)
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun putIcon(iconDto: IconDto?) {
        pluginsCoordinator
            .repository
            .updateIcon(iconDto!!)
    }

    @get:Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @get:GET
    val allIconCategories: List<IconDto>
        get() = pluginsCoordinator
                    .repository
                    .getAllIcons()

    @GET
    @Path("/{id}/raw")
    @Produces("image/svg+xml;charset=utf-8")
    fun getRaw(@PathParam("id") id: Long): String {
        return pluginsCoordinator
                    .repository
                    .getIcon(id).raw
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun deleteIcon(@PathParam("id") id: Long) {
        pluginsCoordinator
            .repository
            .deleteIcon(id)
    }
}