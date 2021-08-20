package eu.geekhome.domain.events

class LiveEvent<T: LiveEventData>(
    val timestamp: Long,
    val number: Int,
    val type: String,
    val data: T
)