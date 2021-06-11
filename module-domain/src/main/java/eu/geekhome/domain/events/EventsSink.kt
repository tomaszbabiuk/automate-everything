package eu.geekhome.domain.events

import java.util.function.Predicate

interface LiveEventsListener {
    fun onEvent(event: LiveEvent<*>)
}

interface EventsSink {
    fun addAdapterEventListener(listener: LiveEventsListener)
    fun removeListener(listener: LiveEventsListener)
    fun broadcastEvent(payload: LiveEventData)
    fun reset()
    fun removeRange(filter: Predicate<in LiveEvent<*>>)
    fun all() : List<LiveEvent<*>>
}