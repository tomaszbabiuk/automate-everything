package eu.geekhome.rest.hardware

import eu.geekhome.HardwareManager
import eu.geekhome.rest.HardwareManagerHolderService
import eu.geekhome.services.events.HardwareEvent
import eu.geekhome.services.hardware.DiscoveryEventDto
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("adapterevents")
class AdapterEvents @Inject constructor(
    hardwareManagerHolderService: HardwareManagerHolderService,
    private val hardwareEventMapper: NumberedHardwareEventToEventDtoMapper,
) {

    private val hardwareManager: HardwareManager = hardwareManagerHolderService.instance

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getEvents(): List<DiscoveryEventDto> {
        return hardwareManager
            .discoverySink
            .all()
            .filter { it.data is HardwareEvent }
            .map { hardwareEventMapper.map(it.number, it.data as HardwareEvent) }
            .toList()
    }
}