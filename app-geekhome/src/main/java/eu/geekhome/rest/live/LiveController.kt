package eu.geekhome.rest.live

import eu.geekhome.HardwareManager
import eu.geekhome.automation.AutomationConductor
import eu.geekhome.rest.AutomationConductorHolderService
import eu.geekhome.rest.HardwareManagerHolderService
import eu.geekhome.rest.PluginsCoordinator
import eu.geekhome.rest.hardware.NumberedHardwareEventToEventDtoMapper
import eu.geekhome.rest.hardware.PortDtoMapper
import eu.geekhome.rest.plugins.PluginDtoMapper
import eu.geekhome.services.events.*
import org.pf4j.PluginStateEvent
import org.pf4j.PluginStateListener
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
    automationHolder: AutomationConductorHolderService,
    pluginsCoordinator: PluginsCoordinator,
    hardwareManagerHolderService: HardwareManagerHolderService,
    private val portDtoMapper: PortDtoMapper,
    private val pluginDtoMapper: PluginDtoMapper,
    private val hardwareEventMapper: NumberedHardwareEventToEventDtoMapper,
    private val sse: Sse
) : PluginStateListener {
    private var automation: AutomationConductor = automationHolder.instance
    private val sseBroadcaster = sse.newBroadcaster()
    private val hardwareManager: HardwareManager = hardwareManagerHolderService.instance

    init {
        automation.liveEvents.addAdapterEventListener(object : LiveEventsListener {
            override fun onEvent(event: LiveEvent<*>) {
                broadcastLiveEvent(event)
            }
        })

        pluginsCoordinator.registerStateListener(this)

        hardwareManager.discoverySink.addAdapterEventListener(object : LiveEventsListener {
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
        val mapped: Any? = when (event.data) {
            is PortUpdateEvent -> {
                val payload = event.data as PortUpdateEvent
                portDtoMapper.map(payload.port, payload.factoryId, payload.adapterId)
            }
            is PluginEvent -> {
                val payload = event.data as PluginEvent
                pluginDtoMapper.map(payload.plugin)
            }
            is HardwareEvent -> {
                val payload = event.data as HardwareEvent
                hardwareEventMapper.map(event.number, payload)
            }
            else -> null
        }

        if (mapped != null) {
            broadcast(mapped.javaClass, mapped)
        }
    }

    override fun pluginStateChanged(event: PluginStateEvent?) {
        val plugin = event!!.plugin
        val payload = PluginEvent(plugin)
        broadcastLiveEvent(LiveEvent(0, PluginEvent::class.java.simpleName, payload))
    }
}