package eu.automateeverything.domain.events

import java.util.*
import java.util.function.Predicate
import kotlin.collections.ArrayList

class NumberedEventsSink : EventsSink {

    var eventCounter = 0

    private val listeners = Collections.synchronizedList(ArrayList<LiveEventsListener>())

    val events = ArrayList<LiveEvent<*>>()
    val messages = ArrayList<LiveEvent<*>>()

    override fun addAdapterEventListener(listener: LiveEventsListener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: LiveEventsListener) {
        listeners.remove(listener)
    }

    private fun enqueue(payload: LiveEventData, target: ArrayList<LiveEvent<*>>, maxSize: Int) {
        if (target.size > maxSize) {
            target.removeAt(0)
        }

        println(payload)

        eventCounter++
        val now = Calendar.getInstance().timeInMillis
        val event = LiveEvent(now, eventCounter, payload.javaClass.simpleName, payload)
        target.add(event)
        listeners.filterNotNull().forEach { listener -> listener.onEvent(event) }
    }

    override fun  broadcastEvent(payload: LiveEventData) {
        enqueue(payload, events, MAX_EVENTS_SIZE)
    }

    override fun broadcastMessage(payload: LiveEventData) {
        enqueue(payload, messages, MAX_INBOX_SIZE)
    }

    override fun reset() {
        eventCounter = 0
        events.clear()
    }

    override fun removeRange(filter: Predicate<in LiveEvent<*>>) {
        events.removeIf(filter)
    }

    override fun all(): List<LiveEvent<*>> {
        return events
    }

    companion object {
        const val MAX_EVENTS_SIZE = 200
        const val MAX_INBOX_SIZE = 100
    }
}