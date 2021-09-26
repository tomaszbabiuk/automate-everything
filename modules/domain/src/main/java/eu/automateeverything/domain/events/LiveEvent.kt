package eu.automateeverything.domain.events

class LiveEvent<T: LiveEventData>(
    val timestamp: Long,
    val number: Int,
    val type: String,
    val data: T
)