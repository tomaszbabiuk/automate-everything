package eu.geekhome.rest.hardware

import eu.geekhome.HardwareManager
import javax.inject.Inject
import eu.geekhome.rest.HardwareManagerHolderService
import eu.geekhome.services.events.NumberedEvent
import eu.geekhome.services.events.NumberedEventsListener
import javax.ws.rs.GET
import javax.ws.rs.Produces
import eu.geekhome.services.hardware.HardwareEvent
import eu.geekhome.services.hardware.HardwareEventDto
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.sse.OutboundSseEvent
import javax.ws.rs.sse.Sse
import javax.ws.rs.sse.SseBroadcaster
import javax.ws.rs.sse.SseEventSink

@Path("discoveryevents")
class DiscoveryEvents @Inject constructor(
    hardwareManagerHolderService: HardwareManagerHolderService,
    private val eventMapper: NumberedHardwareEventToEventDtoMapper,
    private val sse: Sse
) : NumberedEventsListener<HardwareEvent> {

    private val hardwareManager: HardwareManager = hardwareManagerHolderService.instance
    private val sseBroadcaster: SseBroadcaster = sse.newBroadcaster()

    init {
        hardwareManager.sink.addAdapterEventListener(this)
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getEvents(): List<HardwareEventDto> {
        val eventsCount = hardwareManager.sink.getNumberOfEvents()
        if (eventsCount > 0) {
            return (0 until eventsCount)
                .map { hardwareManager.sink.getHistoricEvent(it) }
                .map { eventMapper.map(it) }
                .toList()
        }

        return ArrayList()
    }

    @GET
    @Path("/live")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    fun streamPluginChangesLive(@Context sink: SseEventSink) {
        sseBroadcaster.register(sink)
    }

    override fun onEvent(event: NumberedEvent<HardwareEvent>) {
        broadcastHardwareEvent(event)
    }

    private fun broadcastHardwareEvent(event: NumberedEvent<HardwareEvent>) {
        val dto = eventMapper.map(event)
        val sseEvent: OutboundSseEvent = sse.newEventBuilder()
            .name(dto.factoryId)
            .id(dto.no.toString())
            .mediaType(MediaType.APPLICATION_JSON_TYPE)
            .data(HardwareEventDto::class.java, dto)
            .build()
        sseBroadcaster.broadcast(sseEvent)
    }
}