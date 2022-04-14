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