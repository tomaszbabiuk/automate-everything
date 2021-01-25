package eu.geekhome.services.events

import java.util.function.Predicate

data class NumberedEvent<T>(val no: Int, val payload: T)

interface NumberedEventsListener<T> {
    fun onEvent(event: NumberedEvent<T>)
}

interface EventsSink<T> {
    fun getNumberOfEvents(): Int
    fun getHistoricEvent(id: Int) : NumberedEvent<T>
    fun addAdapterEventListener(listener: NumberedEventsListener<T>)
    fun removeListener(listener: NumberedEventsListener<T>)
    fun broadcastEvent(payload: T)
    fun reset()
    fun removeRange(filter: Predicate<in NumberedEvent<T>>)
}