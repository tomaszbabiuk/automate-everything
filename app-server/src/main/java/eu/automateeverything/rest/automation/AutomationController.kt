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

