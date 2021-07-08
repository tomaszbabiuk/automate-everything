package eu.geekhome.data.tags

data class TagDto(
    val id: Long,
    val parentId: Long?,
    val name: String
    )