package eu.geekhome.rest.tags

import eu.geekhome.domain.repository.Repository
import javax.inject.Inject
import eu.geekhome.domain.repository.TagDto
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("tags")
class TagsController @Inject constructor(private val repository: Repository) {

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun postTag(tagDto: TagDto?): Long {
        return repository
            .saveTag(tagDto!!)
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun putTag(tagDto: TagDto?) {
        repository
            .updateTag(tagDto!!)
    }

    @get:Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @get:GET
    val allTags: List<TagDto>
        get() = repository
                    .getAllTags()

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun deleteTag(@PathParam("id") id: Long) {
        repository
            .deleteTag(id)
    }
}