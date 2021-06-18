package eu.geekhome.domain.repository

data class InstanceDto(
    val id: Long,
    val iconId: Long?,
    val tagIds: List<Long>,
    val clazz: String,
    val fields: Map<String, String?>,
    val automation: String?
)