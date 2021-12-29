/*
 * Copyright (c) 2019-2021 Tomasz Babiuk
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

package eu.automateeverything.rest.icons

import eu.automateeverything.data.Repository
import eu.automateeverything.data.icons.IconDto
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType

@Path("icons")
class IconController @Inject constructor(private val repository: Repository) {

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun postIcon(iconDto: IconDto): Long {
        return repository
                    .saveIcon(iconDto)
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun putIcon(iconDto: IconDto) {
        repository
            .updateIcon(iconDto)
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