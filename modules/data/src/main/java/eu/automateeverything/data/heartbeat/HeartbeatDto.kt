package eu.automateeverything.data.heartbeat

data class HeartbeatDto(
    val timestamp: Long,
    val inboxUnreadCount : Int,
    val isAutomationEnabled: Boolean)