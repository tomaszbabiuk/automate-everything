package eu.geekhome.rest.automationunits

import eu.geekhome.automation.AutomationConductor
import eu.geekhome.rest.AutomationConductorHolderService
import eu.geekhome.rest.automation.AutomationUnitDto
import eu.geekhome.rest.automation.AutomationUnitDtoMapper
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("automationunits")
class AutomationUnitsController @Inject constructor(
    automationHolder: AutomationConductorHolderService,
    private val mapper: AutomationUnitDtoMapper
) {
    private var automation: AutomationConductor = automationHolder.instance

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getAutomation(): List<AutomationUnitDto> {
        return automation
            .automationUnitsCache
            .values
            .map { mapper.map(it.second, it.first) }
    }
}

