package eu.geekhome.domain.repository

data class IconCategoryDto(
    val id: Long,
    val name: String,
    val iconIds: List<Long>
    )