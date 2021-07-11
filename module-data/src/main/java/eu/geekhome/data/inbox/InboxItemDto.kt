package eu.geekhome.data.inbox

data class InboxItemDto(
    val message: String? = null,
    val timestamp: Long,
    val kind: InboxItemKind,
    val newPortId: String? = null,
    val read: Boolean = false
)