package eu.geekhome.services.repository

data class TagDto(
    val id: Long,
    val parentId: Long?,
    val name: String
    )