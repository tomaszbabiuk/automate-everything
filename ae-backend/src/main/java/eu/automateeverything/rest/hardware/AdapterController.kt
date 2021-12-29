package eu.automateeverything.rest.hardware

import eu.automateeverything.domain.hardware.HardwareManager
import eu.automateeverything.domain.events.EventsSink
import jakarta.inject.Inject
import eu.automateeverything.data.hardware.HardwareAdapterDto
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType

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