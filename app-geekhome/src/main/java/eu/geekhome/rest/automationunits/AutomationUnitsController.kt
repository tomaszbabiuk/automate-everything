package eu.geekhome.rest.automationunits

import eu.geekhome.domain.automation.AutomationConductor
import eu.geekhome.rest.ResourceNotFoundException
import eu.geekhome.data.automation.AutomationUnitDto
import eu.geekhome.rest.automation.AutomationUnitDtoMapper
import eu.geekhome.domain.automation.StateDeviceAutomationUnit
import eu.geekhome.domain.hardware.HardwareManager
import java.lang.Exception
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

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