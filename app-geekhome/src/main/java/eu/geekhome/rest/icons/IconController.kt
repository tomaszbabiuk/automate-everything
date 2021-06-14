package eu.geekhome.rest.icons

import javax.inject.Inject
import eu.geekhome.domain.repository.IconDto
import eu.geekhome.rest.RepositoryHolderService
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("icons")
class IconController @Inject constructor(repositoryHolderService: RepositoryHolderService) {
    private val repository = repositoryHolderService.instance

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun postIcon(iconDto: IconDto?): Long {
        return repository
                    .saveIcon(iconDto!!)
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun putIcon(iconDto: IconDto?) {
        repository
            .updateIcon(iconDto!!)
    }

    @get:Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @get:GET
    val allIconCategories: List<IconDto>
        get() = repository
                    .getAllIcons()

    @GET
    @Path("/{id}/raw")
    @Produces("image/svg+xml;charset=utf-8")
    fun getRaw(@PathParam("id") id: Long): String {
        return repository
                    .getIcon(id).raw
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun deleteIcon(@PathParam("id") id: Long) {
        repository
            .deleteIcon(id)
    }
}