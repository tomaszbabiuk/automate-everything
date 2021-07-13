package eu.geekhome.rest.inbox

import eu.geekhome.data.Repository
import eu.geekhome.data.inbox.InboxMessageDto
import eu.geekhome.domain.inbox.Inbox
import eu.geekhome.rest.ResourceNotFoundException
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("inbox")
class InboxController @Inject constructor(
    private val repository: Repository,
    private val inbox: Inbox,
    private val mapper: InboxMessageDtoMapper) {

    @get:Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @get:GET
    val allInboxItems: List<InboxMessageDto>
        get() = repository
            .getAllInboxItems()
            .map(mapper::map)

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun deleteIcon(@PathParam("id") id: Long?) {
        if (id == null) {
            throw ResourceNotFoundException()
        }

        repository
            .deleteInboxItem(id)
    }

    @PUT
    @Path("/{id}/read")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    fun updateRead(@PathParam("id") id: Long?, read: Boolean): InboxMessageDto {
        if (id == null) {
            throw ResourceNotFoundException()
        }

        val x = repository.markInboxItemAsRead(id)
        inbox.refreshUnreadMessages()
        return mapper.map(x)
    }
}