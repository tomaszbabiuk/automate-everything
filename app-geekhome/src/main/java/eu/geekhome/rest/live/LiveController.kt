package eu.geekhome.rest.live

import eu.geekhome.rest.*
import eu.geekhome.rest.hardware.NumberedHardwareEventToEventDtoMapper
import eu.geekhome.rest.hardware.PortDtoMapper
import eu.geekhome.rest.plugins.PluginDtoMapper
import eu.geekhome.services.events.*
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.sse.OutboundSseEvent
import javax.ws.rs.sse.Sse
import javax.ws.rs.sse.SseEventSink

@Path("live")
class LiveController @Inject constructor(
    eventsSinkHolder: EventsSinkHolderService,
    private val portDtoMapper: PortDtoMapper,
    private val pluginDtoMapper: PluginDtoMapper,
    private val hardwareEventMapper: NumberedHardwareEventToEventDtoMapper,
    private val sse: Sse
) {
    private val sseBroadcaster = sse.newBroadcaster()
    private val eventsSink = eventsSinkHolder.instance

    init {
        eventsSink.addAdapterEventListener(object : LiveEventsListener {
            override fun onEvent(event: LiveEvent<*>) {
                broadcastLiveEvent(event)
            }
        })
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    fun streamPluginChangesLive(@Context sink: SseEventSink?) {
        sseBroadcaster.register(sink)
    }

//    @GET
//    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
//    fun getEvents(@QueryParam("type") type: String): List<*> {
//        if (type.toLowerCase() == "discovery") {
//            return hardwareManager
//                .discoverySink
//                .all()
//                .filter { it.data is DiscoveryEvent }
//                .map { hardwareEventMapper.map(it.number, it.data as DiscoveryEvent) }
//                .toList()
//        }
//
//        if (type.toLowerCase() == "plugin") {
//            return automation
//                .liveEvents
//                .all()
//                .filter { it.data is PortUpdateEvent }
//                .map { it.data as PortUpdateEvent}
//                .map { portDtoMapper.map(it.port, it.factoryId, it.adapterId) }
//                .toList()
//        }
//
//        throw ResourceNotFoundException()
//    }

    private fun broadcast(clazz: Class<*>, obj: Any) {
        val sseEvent: OutboundSseEvent = sse.newEventBuilder()
            .name(obj.javaClass.simpleName)
            .mediaType(MediaType.APPLICATION_JSON_TYPE)
            .data(clazz, obj)
            .build()
        sseBroadcaster.broadcast(sseEvent)
    }

    private fun broadcastLiveEvent(event: LiveEvent<*>) {
        val mapped: Any? = when (event.data) {
            is PortUpdateEvent -> {
                val payload = event.data as PortUpdateEvent
                portDtoMapper.map(payload.port, payload.factoryId, payload.adapterId)
            }
            is PluginEvent -> {
                val payload = event.data as PluginEvent
                pluginDtoMapper.map(payload.plugin)
            }
            is DiscoveryEvent -> {
                val payload = event.data as DiscoveryEvent
                hardwareEventMapper.map(event.number, payload)
            }
            else -> null
        }

        if (mapped != null) {
            broadcast(mapped.javaClass, mapped)
        }
    }
}