package eu.automateeverything.rest.inbox

import eu.automateeverything.data.Repository
import eu.automateeverything.data.inbox.InboxMessageDto
import eu.automateeverything.domain.inbox.Inbox
import eu.automateeverything.rest.ResourceNotFoundException
import jakarta.servlet.http.HttpServletResponse
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType

@Path("inbox")
class InboxController @Inject constructor(
    private val repository: Repository,
    private val inbox: Inbox,
    private val mapper: InboxMessageDtoMapper) {


    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun allInboxItems(@QueryParam("limit") limit: Long,
                      @QueryParam("offset") offset: Long,
                      @Context response: HttpServletResponse
    ): List<InboxMessageDto> {

        response.addHeader("X-Total-Count", repository.countInboxItems().toString())

        return repository
            .getInboxItems(if (limit == 0L) 20 else limit, offset)
            .map(mapper::map)
    }

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
    fun updateRead(@PathParam("id") id: Long?): InboxMessageDto {
        if (id == null) {
            throw ResourceNotFoundException()
        }

        val x = repository.markInboxItemAsRead(id)
        inbox.refreshUnreadMessages()
        return mapper.map(x)
    }
}