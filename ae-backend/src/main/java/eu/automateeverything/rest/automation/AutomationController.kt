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

package eu.automateeverything.rest.automation

import eu.automateeverything.data.automation.AutomationDto
import eu.automateeverything.domain.automation.AutomationConductor
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("automation")
class AutomationController @Inject constructor(
    private val automationConductor: AutomationConductor
) {
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getAutomation(): AutomationDto {
        return AutomationDto(automationConductor.isEnabled())
    }

    @PUT
    @Path("/enabled")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    fun updateEnableState(enable: Boolean): AutomationDto {
        if (enable) {
            automationConductor.enable()
        } else {
            automationConductor.disable()
        }

        return AutomationDto(automationConductor.isEnabled())
    }
}

