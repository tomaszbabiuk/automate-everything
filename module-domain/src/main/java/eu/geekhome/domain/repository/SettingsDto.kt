package eu.geekhome.domain.repository

data class SettingsDto(
    val pluginId: String,
    val clazz: String,
    val fields: Map<String, String?>,
)