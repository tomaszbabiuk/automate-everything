package eu.automateeverything.domain.extensibility

import eu.automateeverything.domain.configurable.SettingGroup
import eu.automateeverything.data.localization.Resource

interface PluginMetadata {
    val name: Resource
    val description: Resource
    val settingGroups: List<SettingGroup>
        get() = listOf()
}