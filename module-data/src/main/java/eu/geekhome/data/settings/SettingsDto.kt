package eu.geekhome.data.settings

data class SettingsDto(
    val pluginId: String,
    val clazz: String,
    val fields: Map<String, String?>,
)