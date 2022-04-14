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

package eu.automateeverything.rest.hardware

import eu.automateeverything.domain.hardware.HardwareManager
import eu.automateeverything.domain.events.EventsSink
import jakarta.inject.Inject
import eu.automateeverything.data.hardware.HardwareAdapterDto
import eu.automateeverything.mappers.HardwareAdapterDtoMapper
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType

@Path("hardwareadapters")
class AdapterController @Inject constructor(
    private val hardwareManager: HardwareManager,
    private val mapper: HardwareAdapterDtoMapper,
    private val liveEvent: EventsSink
) {

    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @GET
    fun getAdapters(): List<HardwareAdapterDto> {
        return hardwareManager
            .bundles()
            .map { mapper.map(it) }
    }

    @PUT
    @Path("/{factoryId}/discover")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    fun discover(@PathParam("factoryId") factoryId: String) {
        hardwareManager.scheduleDiscovery(factoryId)
    }
}