package eu.geekhome.services.events

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
}