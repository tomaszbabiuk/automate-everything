package eu.geekhome.data.plugins

import eu.geekhome.data.localization.Resource
import eu.geekhome.data.settings.SettingGroupDto

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