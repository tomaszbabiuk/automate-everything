package eu.geekhome.rest.hardware

import eu.geekhome.HardwareManager
import javax.inject.Inject
import eu.geekhome.rest.HardwareManagerHolderService
import eu.geekhome.rest.ResourceNotFoundException
import eu.geekhome.services.hardware.*
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("ports")
class PortsController @Inject constructor(
    hardwareManagerHolderService: HardwareManagerHolderService,
    val portDtoMapper: PortDtoMapper
) {
    private val hardwareManager: HardwareManager = hardwareManagerHolderService.instance

    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @GET
    fun getAdapters() : List<PortDto> {
        return hardwareManager
            .bundles()
            .flatMap {
                val factoryId = it.factoryId
                it.ports.map { port -> Triple(port, factoryId, it.adapter.id) }
            }
            .map { portDtoMapper.map(it.first, it.second, it.third) }
    }

    @Suppress("UNCHECKED_CAST")
    @PUT
    @Path("/{id}/value")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    fun updateValue(@PathParam("id") id: String, value: Int) {
        val findings = hardwareManager.findPort(id)
        if (findings != null) {
            val port = findings.first
            val bundle = findings.second



            if (port.valueType.name == Relay::class.java.name) {
                val newValue = Relay.fromInteger(value)
                (port as Port<Relay>).write(newValue)
            } else if (port.valueType.name == PowerLevel::class.java.name) {
                val newValue = PowerLevel.fromInteger(value)
                (port as Port<PowerLevel>).write(newValue)
            } else {
                //TODO
            }
            bundle.adapter.executePendingChanges()
        } else {
            throw ResourceNotFoundException()
        }
    }
}
