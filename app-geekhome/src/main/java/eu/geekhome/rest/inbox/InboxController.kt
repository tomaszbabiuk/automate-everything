package eu.geekhome.rest.inbox

import eu.geekhome.data.Repository
import eu.geekhome.data.inbox.InboxItemDto
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("inbox")
class InboxController @Inject constructor(private val repository: Repository) {

    @get:Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @get:GET
    val allInboxItems: List<InboxItemDto>
        get() = repository.getAllInboxItems()

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun deleteIcon(@PathParam("id") id: Long) {
        repository
            .deleteInboxItem(id)
    }
}