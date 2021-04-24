package eu.geekhome.services.events

import java.util.*
import java.util.function.Predicate
import kotlin.collections.ArrayList

class NumberedEventsSink : EventsSink {

    var eventCounter = 0

    private val listeners = Collections.synchronizedList(ArrayList<LiveEventsListener>())

    val events = ArrayList<LiveEvent<*>>()

    override fun addAdapterEventListener(listener: LiveEventsListener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: LiveEventsListener) {
        listeners.remove(listener)
    }

    override fun  broadcastEvent(payload: LiveEventData) {
        if (events.size > MAX_SIZE) {
            events.removeAt(0)
        }

        println("broadcasting event: $payload")

        eventCounter++
        val now = Calendar.getInstance().timeInMillis
        val event = LiveEvent(now, eventCounter, payload.javaClass.simpleName, payload)
        events.add(event)
        listeners.filterNotNull().forEach { listener -> listener.onEvent(event) }
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
        const val MAX_SIZE = 100
    }
}