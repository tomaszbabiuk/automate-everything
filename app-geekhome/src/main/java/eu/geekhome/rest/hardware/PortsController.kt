package eu.geekhome.rest.hardware

import eu.geekhome.data.Repository
import eu.geekhome.data.hardware.PortDto
import eu.geekhome.domain.hardware.HardwareManager
import javax.inject.Inject
import eu.geekhome.rest.ResourceNotFoundException
import eu.geekhome.domain.hardware.*
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("ports")
class PortsController @Inject constructor(
    private val hardwareManager: HardwareManager,
    private val repository: Repository,
    private val portDtoMapper: PortDtoMapper
) {

    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @GET
    fun getPorts() : List<PortDto> {
        val portsInRepo = repository.getAllPorts().toMutableList()

        hardwareManager.checkNewPorts()

        val portsInHardware = hardwareManager
            .bundles()
            .flatMap {
                val factoryId = it.owningPluginId
                it
                    .adapter
                    .ports
                    .map { port -> Triple(port.value, factoryId, it.adapter.id) }
            }
            .map { portDtoMapper.map(it.first, it.second, it.third) }

        portsInHardware
            .forEach { hardwarePort -> portsInRepo.removeIf { it.id == hardwarePort.id} }

        portsInRepo.addAll(portsInHardware)

        return portsInRepo;
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
                (port as OutputPort<Relay>).write(newValue)
            } else if (port.valueType.name == PowerLevel::class.java.name) {
                val newValue = PowerLevel.fromInteger(value)
                (port as OutputPort<PowerLevel>).write(newValue)
            } else {
                //TODO
            }
            //TODO
            bundle.adapter.executePendingChanges()
        } else {
            throw ResourceNotFoundException()
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun deleteTag(@PathParam("id") id: String) {
        repository
            .deletePort(id)
    }
}
