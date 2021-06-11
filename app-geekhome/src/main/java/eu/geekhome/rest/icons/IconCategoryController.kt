package eu.geekhome.rest.icons

import eu.geekhome.PluginsCoordinator
import javax.inject.Inject
import eu.geekhome.rest.PluginsCoordinatorHolderService
import eu.geekhome.domain.repository.IconCategoryDto
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("iconcategories")
class IconCategoryController @Inject constructor(
    pluginsCoordinatorHolderService: PluginsCoordinatorHolderService) {

    private val pluginsCoordinator: PluginsCoordinator = pluginsCoordinatorHolderService.instance

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun postIconCategory(iconCategoryDto: IconCategoryDto?): Long {
        return pluginsCoordinator
                    .repository
                    .saveIconCategory(iconCategoryDto!!)
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun putIconCategory(iconCategoryDto: IconCategoryDto?) {
        pluginsCoordinator
            .repository
            .updateIconCategory(iconCategoryDto!!)
    }

    @get:Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @get:GET
    val allIConCategories: List<IconCategoryDto>
        get() = pluginsCoordinator
                    .repository
                    .getAllIconCategories()

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun deleteIconCategory(@PathParam("id") id: Long) {
        pluginsCoordinator
            .repository
            .deleteIconCategory(id)
    }
}