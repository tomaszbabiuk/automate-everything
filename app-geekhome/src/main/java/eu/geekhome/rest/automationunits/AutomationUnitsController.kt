package eu.geekhome.rest.automationunits

import eu.geekhome.automation.AutomationConductor
import eu.geekhome.rest.AutomationConductorHolderService
import eu.geekhome.rest.ResourceNotFoundException
import eu.geekhome.rest.automation.AutomationUnitDto
import eu.geekhome.rest.automation.AutomationUnitDtoMapper
import eu.geekhome.services.automation.ControlMode
import eu.geekhome.services.automation.StateDeviceAutomationUnit
import java.lang.Exception
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("automationunits")
class AutomationUnitsController @Inject constructor(
    automationHolder: AutomationConductorHolderService,
    private val mapper: AutomationUnitDtoMapper
) {
    private var automation: AutomationConductor = automationHolder.instance

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun listAllAutomationUnits(): List<AutomationUnitDto> {
        return automation
            .automationUnitsCache
            .values
            .map {
                mapper.map(it.second, it.first)
            }
    }

    @PUT
    @Path("/{instanceId}/state")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    fun updateState(@PathParam("instanceId") id: Long, state: String): AutomationUnitDto {
        val instanceAndUnitPair = automation
            .automationUnitsCache
            .filter { it.key == id }
            .map { it.value }
            .firstOrNull() ?: throw ResourceNotFoundException()

        val instance = instanceAndUnitPair.first
        val unit = instanceAndUnitPair.second as? StateDeviceAutomationUnit
        if (unit != null) {
            unit.changeState(state, ControlMode.Manual)
        } else {
            throw Exception("Invalid automation unit class, ${StateDeviceAutomationUnit::class.java.simpleName} expected")
        }

        return mapper.map(unit, instance)
    }
}