package eu.geekhome.rest.hardware

import eu.geekhome.HardwareManager
import javax.inject.Inject
import eu.geekhome.rest.HardwareManagerHolderService
import eu.geekhome.rest.ResourceNotFoundException
import eu.geekhome.services.hardware.Port
import eu.geekhome.services.hardware.PortDto
import eu.geekhome.services.hardware.Relay
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("ports")
class PortsController @Inject constructor(
    hardwareManagerHolderService: HardwareManagerHolderService,
    portDtoMapper: PortDtoMapper,
    private val mapper: PortDtoMapper
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
            .map { mapper.map(it.first, it.second, it.third) }
    }

    @PUT
    @Path("/{id}/value")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    fun updateValue(@PathParam("id") id: String, value: Boolean): PortDto {
        val findings = hardwareManager.findPort(id)
        if (findings != null) {
            val port = findings.first
            val bundle = findings.second
            if (port.valueType.javaClass == Relay::class.java.javaClass) {
                val newValue = Relay(value)
                (port as Port<Boolean, Relay>).write(newValue)
                bundle.adapter.executePendingChanges()

                return mapper.map(port, bundle.factoryId, bundle.adapter.id)
            }
        }

        throw ResourceNotFoundException()
    }
}