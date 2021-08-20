package eu.geekhome.domain.extensibility

import eu.geekhome.domain.configurable.SettingGroup
import eu.geekhome.data.localization.Resource

interface PluginMetadata {
    val name: Resource
    val description: Resource
    val settingGroups: List<SettingGroup>
        get() = listOf()
}