package eu.geekhome.data.heartbeat

data class HeartbeatDto(
    val timestamp: Long,
    val unreadMessagesCount : Int,
    val isAutomationEnabled: Boolean)