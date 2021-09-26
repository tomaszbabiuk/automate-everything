package eu.automateeverything.data.fields

data class FieldInstanceDto(
    val id: Long,
    val name: String,
    val valueAsString: String,
    val instanceId: Long
)