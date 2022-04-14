/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything.rest.inbox

import eu.automateeverything.data.Repository
import eu.automateeverything.data.inbox.InboxMessageDto
import eu.automateeverything.domain.inbox.Inbox
import eu.automateeverything.domain.ResourceNotFoundException
import eu.automateeverything.mappers.InboxMessageDtoMapper
import jakarta.servlet.http.HttpServletResponse
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType

@Path("inbox")
class InboxController @Inject constructor(
    private val repository: Repository,
    private val inbox: Inbox,
    private val mapper: InboxMessageDtoMapper
) {


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