package eu.geekhome.domain.repository

data class SettingsDto(
    val clazz: String,
    val fields: Map<String, String?>,
)