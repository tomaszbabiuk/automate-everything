package eu.geekhome.rest.hardware

import javax.inject.Inject
import eu.geekhome.rest.HardwareManagerHolderService
import eu.geekhome.rest.PluginsCoordinatorHolderService
import eu.geekhome.rest.ResourceNotFoundException
import eu.geekhome.domain.hardware.*
import eu.geekhome.rest.RepositoryHolderService
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("ports")
class PortsController @Inject constructor(
    val pluginsCoordinatorHolderService: PluginsCoordinatorHolderService,
    hardwareManagerHolderService: HardwareManagerHolderService,
    repositoryHolderService: RepositoryHolderService,
    private val portDtoMapper: PortDtoMapper
) {
    private val hardwareManager = hardwareManagerHolderService.instance
    private val pluginsCoordinator = pluginsCoordinatorHolderService.instance
    private val repository = repositoryHolderService.instance

    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @GET
    fun getPorts() : List<PortDto> {
        val portsInRepo = repository.getAllPorts().toMutableList()

        hardwareManager.checkNewPorts()

        val portsInHardware = hardwareManager
            .bundles()
            .flatMap {
                val factoryId = it.factoryId
                it
                    .ports
                    .map { port -> Triple(port, factoryId, it.adapter.id) }
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
                (port as Port<Relay>).write(newValue)
            } else if (port.valueType.name == PowerLevel::class.java.name) {
                val newValue = PowerLevel.fromInteger(value)
                (port as Port<PowerLevel>).write(newValue)
            } else {
                //TODO
            }
            //TODO
            bundle.adapter.executePendingChanges()
        } else {
            throw ResourceNotFoundException()
        }
    }
}
