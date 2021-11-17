package eu.automateeverything.domain.dependencies

data class Dependency(
    val type: DependencyType,
    val name: String?,
    val level: Int
)