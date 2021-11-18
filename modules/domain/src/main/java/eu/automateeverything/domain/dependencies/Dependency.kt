package eu.automateeverything.domain.dependencies

data class Dependency(
    val id: Long,
    val type: DependencyType,
    val name: String?,
)