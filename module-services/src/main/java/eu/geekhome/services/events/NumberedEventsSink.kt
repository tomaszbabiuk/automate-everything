package eu.geekhome.services.events

class NumberedEventsSink<T> : EventsSink<T> {

    var eventCounter = 0
    val listeners = ArrayList<NumberedEventsListener<T>>()

    val events = ArrayList<NumberedEvent<T>>()

    override fun addAdapterEventListener(listener: NumberedEventsListener<T>) {
        listeners.add(listener)
    }

    override fun removeListener(listener: NumberedEventsListener<T>) {
        listeners.remove(listener)
    }

    override fun broadcastEvent(payload: T) {
        println("broadcasting event: $payload")

        eventCounter++
        val event = NumberedEvent(eventCounter, payload)
        events.add(event)
        listeners.forEach { listener -> listener.onEvent(event) }
    }

    override fun reset() {
        eventCounter = 0
        events.clear()
    }

    override fun getNumberOfEvents(): Int {
        return events.size
    }

    override fun getHistoricEvent(id: Int): NumberedEvent<T> {
        return events[id]
    }
}