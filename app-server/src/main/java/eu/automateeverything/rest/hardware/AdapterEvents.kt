package eu.automateeverything.rest.hardware

import eu.automateeverything.domain.events.DiscoveryEventData
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.data.hardware.DiscoveryEventDto
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("adapterevents")
class AdapterEvents @Inject constructor(
    private val eventsSink: EventsSink,
    private val hardwareEventMapper: NumberedHardwareEventToEventDtoMapper,
) {

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getEvents(): List<DiscoveryEventDto> {
        return eventsSink
            .all()
            .filter { it.data is DiscoveryEventData }
            .map { hardwareEventMapper.map(it.number, it.data as DiscoveryEventData) }
            .toList()
    }
}