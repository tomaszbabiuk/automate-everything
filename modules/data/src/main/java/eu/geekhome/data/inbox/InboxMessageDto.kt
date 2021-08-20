package eu.geekhome.data.inbox

import eu.geekhome.data.localization.Resource

data class InboxMessageDto(
    val id: Long,
    val subject: Resource,
    val body: Resource,
    val timestamp: Long,
    val read: Boolean = false
)