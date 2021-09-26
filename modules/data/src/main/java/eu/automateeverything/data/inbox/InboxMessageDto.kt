package eu.automateeverything.data.inbox

import eu.automateeverything.data.localization.Resource

data class InboxMessageDto(
    val id: Long,
    val subject: Resource,
    val body: Resource,
    val timestamp: Long,
    val read: Boolean = false
)