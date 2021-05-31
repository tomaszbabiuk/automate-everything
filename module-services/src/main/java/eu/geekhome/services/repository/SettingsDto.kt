package eu.geekhome.services.repository

data class SettingsDto(
    val clazz: String,
    val fields: Map<String, String?>,
)