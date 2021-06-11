package eu.geekhome.rest.plugins

import eu.geekhome.domain.configurable.SettingGroupDto
import eu.geekhome.domain.localization.Resource

data class PluginDto(
    val id: String,
    val name: Resource,
    val description: Resource,
    val provider: String,
    val version: String,
    val isHardwareFactory: Boolean,
    val enabled: Boolean,
    val settingGroups: List<SettingGroupDto>?
)