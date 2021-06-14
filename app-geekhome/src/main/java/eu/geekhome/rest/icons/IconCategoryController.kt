package eu.geekhome.rest.icons

import javax.inject.Inject
import eu.geekhome.domain.repository.IconCategoryDto
import eu.geekhome.rest.RepositoryHolderService
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("iconcategories")
class IconCategoryController @Inject constructor(
    repositoryHolderService: RepositoryHolderService) {

    private val repository = repositoryHolderService.instance

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun postIconCategory(iconCategoryDto: IconCategoryDto?): Long {
        return repository
                    .saveIconCategory(iconCategoryDto!!)
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun putIconCategory(iconCategoryDto: IconCategoryDto?) {
        repository
            .updateIconCategory(iconCategoryDto!!)
    }

    @get:Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @get:GET
    val allIConCategories: List<IconCategoryDto>
        get() = repository
                    .getAllIconCategories()

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun deleteIconCategory(@PathParam("id") id: Long) {
        repository
            .deleteIconCategory(id)
    }
}