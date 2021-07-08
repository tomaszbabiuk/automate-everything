package eu.geekhome.rest.hardware

import eu.geekhome.domain.hardware.HardwareManager
import eu.geekhome.domain.events.EventsSink
import javax.inject.Inject
import eu.geekhome.data.hardware.HardwareAdapterDto
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("hardwareadapters")
class AdapterController @Inject constructor(
    private val hardwareManager: HardwareManager,
    private val mapper: HardwareAdapterDtoMapper,
    private val liveEvent: EventsSink
) {

    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @GET
    fun getAdapters(): List<HardwareAdapterDto> {
        return hardwareManager
            .bundles()
            .map { mapper.map(it) }
    }

    @PUT
    @Path("/{factoryId}/discover")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    fun discover(@PathParam("factoryId") factoryId: String) {
        hardwareManager.scheduleDiscovery(factoryId)
    }
}