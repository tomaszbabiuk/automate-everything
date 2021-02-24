package eu.geekhome.rest.automation

import eu.geekhome.automation.AutomationConductor
import eu.geekhome.rest.AutomationConductorHolderService
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("automation")
class AutomationController @Inject constructor(
    automationHolder: AutomationConductorHolderService
) {
    private var automation: AutomationConductor = automationHolder.instance

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getAutomation(): AutomationDto {
        return AutomationDto(automation.isEnabled())
    }

    @PUT
    @Path("/enabled")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    fun updateEnableState(enable: Boolean): AutomationDto {
        if (enable) {
            automation.enable()
        } else {
            automation.disable()
        }

        return AutomationDto(automation.isEnabled())
    }
}

