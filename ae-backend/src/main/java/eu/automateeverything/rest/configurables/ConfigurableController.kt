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

package eu.automateeverything.rest.configurables

import eu.automateeverything.data.configurables.ConfigurableDto
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.ServerException
import eu.automateeverything.domain.configurable.GeneratedConfigurable
import eu.automateeverything.domain.extensibility.PluginsCoordinator
import eu.automateeverything.mappers.ConfigurableDtoMapper
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import java.util.stream.Collectors

@Path("configurables")
class ConfigurableController @Inject constructor(
    private val pluginsCoordinator: PluginsCoordinator,
    private val configurableDtoMapper: ConfigurableDtoMapper
) {

    private fun findConfigurable(clazz: String): GeneratedConfigurable? {
    return pluginsCoordinator
        .configurables
        .filter { x -> x.javaClass.name.equals(clazz) }
        .filterIsInstance<GeneratedConfigurable>()
        .firstOrNull()
}


    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getConfigurables(): List<ConfigurableDto> {
        return pluginsCoordinator
            .configurables
            .stream()
            .map { configurableDtoMapper.map(it) }
            .collect(Collectors.toList())
    }

    @POST
    @Path("/{clazz}/generate")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun generate(@PathParam("clazz") clazz: String): InstanceDto {
        val configurable = findConfigurable(clazz)
        if (configurable != null) {
            return configurable.generate()
        }

        throw ServerException("This configurable clazz cannot be generated automatically")
    }
}