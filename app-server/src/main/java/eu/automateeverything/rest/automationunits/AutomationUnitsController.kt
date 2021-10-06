package eu.automateeverything.rest.automationunits

import eu.automateeverything.domain.automation.AutomationConductor
import eu.automateeverything.rest.ResourceNotFoundException
import eu.automateeverything.data.automation.AutomationUnitDto
import eu.automateeverything.rest.automation.AutomationUnitDtoMapper
import eu.automateeverything.domain.automation.StateDeviceAutomationUnit
import eu.automateeverything.domain.hardware.HardwareManager
import java.lang.Exception
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType

@Path("automationunits")
class AutomationUnitsController @Inject constructor(
    private val automationConductor: AutomationConductor,
    private val mapper: AutomationUnitDtoMapper,
    private val hardwareManager: HardwareManager,
    ) {

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun listAllAutomationUnits(): List<AutomationUnitDto> {
        return automationConductor
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
        val instanceAndUnitPair = automationConductor
            .automationUnitsCache
            .filter { it.key == id }
            .map { it.value }
            .firstOrNull() ?: throw ResourceNotFoundException()

        val instance = instanceAndUnitPair.first
        val unit = instanceAndUnitPair.second as? StateDeviceAutomationUnit
        if (unit != null) {
            unit.changeState(state)
            hardwareManager.executeAllPendingChanges()
        } else {
            throw Exception("Invalid automation unit class, ${StateDeviceAutomationUnit::class.java.simpleName} expected")
        }

        return mapper.map(unit, instance)
    }
}