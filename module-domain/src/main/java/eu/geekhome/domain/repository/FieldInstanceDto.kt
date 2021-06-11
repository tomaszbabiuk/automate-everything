package eu.geekhome.domain.repository

data class FieldInstanceDto(
    val id: Long,
    val name: String,
    val valueAsString: String,
    val instanceId: Long
)