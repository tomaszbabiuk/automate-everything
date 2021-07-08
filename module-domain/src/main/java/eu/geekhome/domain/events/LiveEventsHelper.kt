package eu.geekhome.domain.events

import eu.geekhome.domain.hardware.Port

object LiveEventsHelper {
    fun broadcastDiscoveryEvent(eventsSink: EventsSink, factoryId: String, message: String) {
        val event = DiscoveryEventData(factoryId, message)
        eventsSink.broadcastEvent(event)
    }

    fun broadcastPortUpdateEvent(eventsSink: EventsSink, factoryId: String, adapterId: String, port: Port<*>) {
        val event = PortUpdateEventData(factoryId, adapterId, port)
        eventsSink.broadcastEvent(event)
    }

    fun broadcastHeartbeatEvent(eventsSink: EventsSink, timestamp: Long) {
        val event = HeartbeatEventData(timestamp)
        eventsSink.broadcastEvent(event)
    }
}