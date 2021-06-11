package eu.geekhome.services.repository

data class IconCategoryDto(
    val id: Long,
    val name: String,
    val iconIds: List<Long>
    )