package eu.automateeverything.rest.automationunits

import eu.automateeverything.domain.ResourceNotFoundException
import eu.automateeverything.data.automation.AutomationUnitDto
import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.domain.automation.*
import eu.automateeverything.rest.automation.AutomationUnitDtoMapper
import eu.automateeverything.domain.hardware.HardwareManager
import eu.automateeverything.domain.hardware.PortValueBuilder
import java.lang.Exception
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import java.math.BigDecimal

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
            throw Exception("Invalid automation unit class, ${StateDeviceAutomationUnitBase::class.java.simpleName} expected")
        }

        return mapper.map(unit, instance)
    }

    @Suppress("UNCHECKED_CAST")
    @PUT
    @Path("/{instanceId}/control")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    fun updateValue(@PathParam("instanceId") id: Long, newValue: BigDecimal): AutomationUnitDto {
        val instanceAndUnitPair = automationConductor
            .automationUnitsCache
            .filter { it.key == id }
            .map { it.value }
            .firstOrNull() ?: throw ResourceNotFoundException()

        val instance = instanceAndUnitPair.first
        val unit = instanceAndUnitPair.second as? ControllerAutomationUnit<in PortValue>
        if (unit != null) {
            val value = PortValueBuilder.buildFromDecimal(unit.valueClazz, newValue)
            unit.control(value)
            hardwareManager.executeAllPendingChanges()
        } else {
            throw Exception("Invalid automation unit class, ${ControllerAutomationUnit::class.java.simpleName} expected")
        }

        return mapper.map(unit, instance)
    }
}