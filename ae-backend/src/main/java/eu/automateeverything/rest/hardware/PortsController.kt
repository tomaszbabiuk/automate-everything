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

import eu.automateeverything.data.Repository
import eu.automateeverything.data.hardware.PortDto
import eu.automateeverything.domain.hardware.HardwareManager
import jakarta.inject.Inject
import eu.automateeverything.domain.ResourceNotFoundException
import eu.automateeverything.domain.hardware.*
import eu.automateeverything.mappers.PortDtoMapper
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import java.math.BigDecimal

@Path("ports")
class PortsController @Inject constructor(
    private val hardwareManager: HardwareManager,
    private val repository: Repository,
    private val portDtoMapper: PortDtoMapper
) {

    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @GET
    fun getPorts() : List<PortDto> {
        val portsInRepo = repository.getAllPorts().toMutableList()

        hardwareManager.checkNewPorts()

        val portsInHardware = hardwareManager
            .bundles()
            .flatMap {
                val factoryId = it.owningPluginId
                it
                    .adapter
                    .ports
                    .map { port -> Triple(port.value, factoryId, it.adapter.id) }
            }
            .map { portDtoMapper.map(it.first, it.second, it.third) }

        portsInHardware
            .forEach { hardwarePort -> portsInRepo.removeIf { it.id == hardwarePort.id} }

        portsInRepo.addAll(portsInHardware)

        return portsInRepo
    }

    @Suppress("UNCHECKED_CAST")
    @PUT
    @Path("/{id}/value")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    fun updateValue(@PathParam("id") id: String, value: BigDecimal) {
        val findings = hardwareManager.findPort(id)
        if (findings != null) {
            val port = findings.first
            val bundle = findings.second

            if (port is OutputPort) {
                port.write(value)
            }

            bundle.adapter.executePendingChanges()
        } else {
            throw ResourceNotFoundException()
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun deletePort(@PathParam("id") id: String) {
        repository
            .deletePort(id)
    }
}
