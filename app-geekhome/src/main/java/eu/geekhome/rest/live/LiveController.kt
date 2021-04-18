package eu.geekhome.rest.live

import eu.geekhome.rest.*
import eu.geekhome.rest.automation.AutomationUnitDtoMapper
import eu.geekhome.rest.hardware.NumberedHardwareEventToEventDtoMapper
import eu.geekhome.rest.hardware.PortDtoMapper
import eu.geekhome.rest.plugins.PluginDtoMapper
import eu.geekhome.services.events.*
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.NotSupportedException
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
    private val automationUnitMapper: AutomationUnitDtoMapper,
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

    private fun broadcast(clazz: Class<*>, obj: Any) {
        val sseEvent: OutboundSseEvent = sse.newEventBuilder()
            .name(obj.javaClass.simpleName)
            .mediaType(MediaType.APPLICATION_JSON_TYPE)
            .data(clazz, obj)
            .build()
        sseBroadcaster.broadcast(sseEvent)
    }

    private fun broadcastLiveEvent(event: LiveEvent<*>) {
        val mapped: Any = when (event.data) {
            is PortUpdateEventData -> {
                val payload = event.data as PortUpdateEventData
                portDtoMapper.map(payload.port, payload.factoryId, payload.adapterId)
            }
            is PluginEventData -> {
                val payload = event.data as PluginEventData
                pluginDtoMapper.map(payload.plugin)
            }
            is DiscoveryEventData -> {
                val payload = event.data as DiscoveryEventData
                hardwareEventMapper.map(event.number, payload)
            }
            is AutomationUpdateEventData -> {
                val payload = event.data as AutomationUpdateEventData
                automationUnitMapper.map(payload.unit, payload.instance)
            }
            is AutomationStateEventData -> {
                val payload = event.data as AutomationStateEventData
                payload.enabled
            }
        }

        broadcast(mapped.javaClass, mapped)
    }
}