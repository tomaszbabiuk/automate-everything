package eu.geekhome.domain.events

import eu.geekhome.data.inbox.InboxItemDto
import eu.geekhome.domain.hardware.Port
import java.util.function.Predicate

interface LiveEventsListener {
    fun onEvent(event: LiveEvent<*>)
}

interface EventsSink {
    fun addAdapterEventListener(listener: LiveEventsListener)
    fun removeListener(listener: LiveEventsListener)
    fun broadcastEvent(payload: LiveEventData)
    fun broadcastMessage(payload: LiveEventData)
    fun reset()
    fun all() : List<LiveEvent<*>>
    fun removeRange(filter: Predicate<in LiveEvent<*>>)

    fun broadcastDiscoveryEvent(factoryId: String, message: String) {
        val event = DiscoveryEventData(factoryId, message)
        broadcastEvent(event)
    }

    fun broadcastPortUpdateEvent(factoryId: String, adapterId: String, port: Port<*>) {
        val event = PortUpdateEventData(factoryId, adapterId, port)
        broadcastEvent(event)
    }

    fun broadcastHeartbeatEvent(timestamp: Long, unreadMessagesCount: Int, isAutomationEnabled: Boolean) {
        val event = HeartbeatEventData(timestamp, unreadMessagesCount, isAutomationEnabled)
        broadcastEvent(event)
    }

    fun broadcastInboxMessage(inboxItemDto: InboxItemDto) {
        val event = InboxEventData(inboxItemDto)
        broadcastMessage(event)
    }
}