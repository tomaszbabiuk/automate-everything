package eu.automateeverything.data.tags

data class TagDto(
    val id: Long,
    val parentId: Long?,
    val name: String
    )