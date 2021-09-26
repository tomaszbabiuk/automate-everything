package eu.automateeverything.data.inbox

data class InboxItemDto(
    var id: Long = 0L,
    val message: String? = null,
    val timestamp: Long,
    val kind: InboxItemKind,
    val newPortId: String? = null,
    val read: Boolean = false
)