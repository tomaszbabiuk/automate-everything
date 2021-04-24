package eu.geekhome.rest.plugins

import eu.geekhome.services.localization.Resource

data class PluginDto(
    val id: String,
    val name: Resource,
    val description: Resource,
    val provider: String,
    val version: String,
    val isHardwareFactory: Boolean,
    val enabled: Boolean
)