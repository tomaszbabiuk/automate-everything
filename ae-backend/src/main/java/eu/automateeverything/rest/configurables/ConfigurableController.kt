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

package eu.automateeverything.rest.configurables

import eu.automateeverything.domain.extensibility.PluginsCoordinator
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Produces
import eu.automateeverything.data.configurables.ConfigurableDto
import jakarta.servlet.http.HttpServletRequest
import java.util.stream.Collectors
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType

@Path("configurables")
class ConfigurableController @Inject constructor(
    private val pluginsCoordinator: PluginsCoordinator,
    private val configurableDtoMapper: ConfigurableDtoMapper
) {
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getConfigurables(): List<ConfigurableDto> {
        return pluginsCoordinator
            .configurables
            .stream()
            .map { configurableDtoMapper.map(it) }
            .collect(Collectors.toList())
    }
}