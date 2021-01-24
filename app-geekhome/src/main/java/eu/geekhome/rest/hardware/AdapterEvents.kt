package eu.geekhome.rest.hardware

import eu.geekhome.HardwareManager
import javax.inject.Inject
import eu.geekhome.rest.HardwareManagerHolderService
import eu.geekhome.services.events.NumberedEvent
import eu.geekhome.services.events.NumberedEventsListener
import eu.geekhome.services.hardware.HardwareEvent
import javax.ws.rs.GET
import javax.ws.rs.Produces
import eu.geekhome.services.hardware.HardwareEventDto
import eu.geekhome.services.hardware.PortDto
import eu.geekhome.services.hardware.PortUpdateEvent
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.sse.OutboundSseEvent
import javax.ws.rs.sse.Sse
import javax.ws.rs.sse.SseBroadcaster
import javax.ws.rs.sse.SseEventSink

@Path("adapterevents")
class AdapterEvents @Inject constructor(
    hardwareManagerHolderService: HardwareManagerHolderService,
    private val hardwareEventMapper: NumberedHardwareEventToEventDtoMapper,
    private val portDtoMapper: PortDtoMapper,
    private val sse: Sse
) {

    private val hardwareManager: HardwareManager = hardwareManagerHolderService.instance
    private val sseBroadcaster: SseBroadcaster = sse.newBroadcaster()

    init {
        hardwareManager.discoverySink.addAdapterEventListener(object : NumberedEventsListener<HardwareEvent> {
            override fun onEvent(event: NumberedEvent<HardwareEvent>) {
                broadcastHardwareEvent(event)
            }
        })
        hardwareManager.updateSink.addAdapterEventListener(object : NumberedEventsListener<PortUpdateEvent> {
            override fun onEvent(event: NumberedEvent<PortUpdateEvent>) {
                broadcastPortUpdateEvent(event)
            }
        })
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getEvents(): List<HardwareEventDto> {
        val eventsCount = hardwareManager.discoverySink.getNumberOfEvents()
        if (eventsCount > 0) {
            return (0 until eventsCount)
                .map { hardwareManager.discoverySink.getHistoricEvent(it) }
                .map { hardwareEventMapper.map(it) }
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

    private fun broadcastHardwareEvent(event: NumberedEvent<HardwareEvent>) {
        val dto = hardwareEventMapper.map(event)
        val sseEvent: OutboundSseEvent = sse.newEventBuilder()
            .name("discoveryEvent")
            .mediaType(MediaType.APPLICATION_JSON_TYPE)
            .data(HardwareEventDto::class.java, dto)
            .build()
        sseBroadcaster.broadcast(sseEvent)
    }

    private fun broadcastPortUpdateEvent(event: NumberedEvent<PortUpdateEvent>) {
        val dto = portDtoMapper.map(event.payload.port, event.payload.factoryId, event.payload.adapterId)
        val sseEvent: OutboundSseEvent = sse.newEventBuilder()
            .name("portUpdateEvent")
            .mediaType(MediaType.APPLICATION_JSON_TYPE)
            .data(PortDto::class.java, dto)
            .build()
        sseBroadcaster.broadcast(sseEvent)
    }
}