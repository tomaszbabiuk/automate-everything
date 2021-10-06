package eu.automateeverything.rest.icons

import eu.automateeverything.data.Repository
import eu.automateeverything.data.icons.IconCategoryDto
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType

@Path("iconcategories")
class IconCategoryController @Inject constructor(
    private val repository: Repository
) {

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun postIconCategory(iconCategoryDto: IconCategoryDto): Long {
        return repository
                    .saveIconCategory(iconCategoryDto)
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun putIconCategory(iconCategoryDto: IconCategoryDto) {
        repository
            .updateIconCategory(iconCategoryDto)
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