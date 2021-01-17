package eu.geekhome.services.hardware

data class PortDto(
    val id: String,
    val isShadowed: Boolean,
    val isOperational: Boolean,
    val nonOperationalTime : Long,
    val value: String?,
    val canRead: Boolean,
    val canWrite: Boolean
)