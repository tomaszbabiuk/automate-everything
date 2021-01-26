package eu.geekhome.services.events

import java.util.*
import java.util.function.Predicate
import kotlin.collections.ArrayList

class NumberedEventsSink<T> : EventsSink<T> {

    var eventCounter = 0
    private val listeners = Collections.synchronizedList(ArrayList<NumberedEventsListener<T>>())

    val events = ArrayList<NumberedEvent<T>>()

    override fun addAdapterEventListener(listener: NumberedEventsListener<T>) {
        listeners.add(listener)
    }

    override fun removeListener(listener: NumberedEventsListener<T>) {
        listeners.remove(listener)
    }

    override fun broadcastEvent(payload: T) {
        if (events.size > MAX_SIZE) {
            events.removeAt(0)
        }

        println("broadcasting event: $payload")

        eventCounter++
        val event = NumberedEvent(eventCounter, payload)
        events.add(event)
        listeners.filterNotNull().forEach { listener -> listener.onEvent(event) }
    }

    override fun reset() {
        eventCounter = 0
        events.clear()
    }

    override fun removeRange(filter: Predicate<in NumberedEvent<T>>) {
        events.removeIf(filter)
    }

    override fun getNumberOfEvents(): Int {
        return events.size
    }

    override fun getHistoricEvent(id: Int): NumberedEvent<T> {
        return events[id]
    }

    companion object {
        const val MAX_SIZE = 100
    }
}