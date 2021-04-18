package eu.geekhome.rest.hardware

import eu.geekhome.rest.EventsSinkHolderService
import eu.geekhome.services.events.DiscoveryEventData
import eu.geekhome.services.events.EventsSink
import eu.geekhome.services.hardware.DiscoveryEventDto
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("adapterevents")
class AdapterEvents @Inject constructor(
    eventsSinkHolderService: EventsSinkHolderService,
    private val hardwareEventMapper: NumberedHardwareEventToEventDtoMapper,
) {

    private val eventsSink: EventsSink = eventsSinkHolderService.instance

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