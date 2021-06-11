package eu.geekhome.services.extensibility

import eu.geekhome.services.configurable.SettingGroup
import eu.geekhome.services.localization.Resource

interface PluginMetadata {
    val name: Resource
    val description: Resource
    val settingGroups: List<SettingGroup>
        get() = listOf()
}