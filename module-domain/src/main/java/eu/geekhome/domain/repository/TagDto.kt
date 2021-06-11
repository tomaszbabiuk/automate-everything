package eu.geekhome.domain.repository

data class TagDto(
    val id: Long,
    val parentId: Long?,
    val name: String
    )