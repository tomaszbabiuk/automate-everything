package eu.geekhome.rest.live

import eu.geekhome.rest.automation.AutomationUnitDtoMapper
import eu.geekhome.rest.automationhistory.AutomationHistoryDtoMapper
import eu.geekhome.rest.hardware.NumberedHardwareEventToEventDtoMapper
import eu.geekhome.rest.hardware.PortDtoMapper
import eu.geekhome.rest.plugins.PluginDtoMapper
import eu.geekhome.domain.events.*
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
    eventsSink: EventsSink,
    private val portDtoMapper: PortDtoMapper,
    private val pluginDtoMapper: PluginDtoMapper,
    private val hardwareEventMapper: NumberedHardwareEventToEventDtoMapper,
    private val automationUnitMapper: AutomationUnitDtoMapper,
    private val automationHistoryMapper: AutomationHistoryDtoMapper,
    private val heartbeatDtoMapper: HeartbeatDtoMapper,
    private val sse: Sse
) {
    private val sseBroadcaster = sse.newBroadcaster()

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

    private fun broadcast(clazz: Class<*>, obj: Any) {
        val sseEvent: OutboundSseEvent = sse.newEventBuilder()
            .name(obj.javaClass.simpleName)
            .mediaType(MediaType.APPLICATION_JSON_TYPE)
            .data(clazz, obj)
            .build()
        sseBroadcaster.broadcast(sseEvent)
    }

    private fun broadcastLiveEvent(event: LiveEvent<*>) {
        when (event.data) {
            is PortUpdateEventData -> {
                val payload = event.data as PortUpdateEventData
                val mapped = portDtoMapper.map(payload.port, payload.factoryId, payload.adapterId)
                broadcast(mapped.javaClass, mapped)
            }
            is PluginEventData -> {
                val payload = event.data as PluginEventData
                val mapped = pluginDtoMapper.map(payload.plugin)
                broadcast(mapped.javaClass, mapped)
            }
            is DiscoveryEventData -> {
                val payload = event.data as DiscoveryEventData
                val mapped = hardwareEventMapper.map(event.number, payload)
                broadcast(mapped.javaClass, mapped)
            }
            is AutomationUpdateEventData -> {
                val payload = event.data as AutomationUpdateEventData
                val mapped = automationUnitMapper.map(payload.unit, payload.instance)
                broadcast(mapped.javaClass, mapped)

                val payloadHistory = event.data as AutomationUpdateEventData
                val mappedHistory = automationHistoryMapper.map(event.timestamp, payloadHistory, event.number)
                broadcast(mappedHistory.javaClass, mappedHistory)
            }
            is AutomationStateEventData -> {
                val payload = event.data as AutomationStateEventData
                val mapped = payload.enabled
                broadcast(mapped.javaClass, mapped)

                val payloadHistory = event.data as AutomationStateEventData
                val mappedHistory = automationHistoryMapper.map(event.timestamp, payloadHistory, event.number)
                broadcast(mappedHistory.javaClass, mappedHistory)
            }
            is HeartbeatEventData -> {
                val payload = event.data as HeartbeatEventData
                val mapped = heartbeatDtoMapper.map(payload)
                broadcast(mapped.javaClass, mapped)
            }
        }


    }
}