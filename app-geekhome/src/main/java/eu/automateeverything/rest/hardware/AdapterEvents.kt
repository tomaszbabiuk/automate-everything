package eu.automateeverything.rest.hardware

import eu.automateeverything.domain.events.DiscoveryEventData
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.data.hardware.DiscoveryEventDto
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

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