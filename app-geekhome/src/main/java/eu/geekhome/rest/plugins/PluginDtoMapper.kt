package eu.geekhome.rest.plugins

import eu.geekhome.R
import eu.geekhome.rest.settinggroup.SettingGroupDtoMapper
import org.pf4j.PluginWrapper
import org.pf4j.PluginState
import eu.geekhome.domain.hardware.HardwarePlugin
import eu.geekhome.domain.extensibility.PluginMetadata
import javax.inject.Inject

class PluginDtoMapper @Inject constructor(
    private val settingsGroupDtoMapper: SettingGroupDtoMapper,
) {
    fun map(pluginWrapper: PluginWrapper): PluginDto {
        val plugin = pluginWrapper.plugin
        val id = pluginWrapper.pluginId
        val version = pluginWrapper.descriptor.version
        val provider = pluginWrapper.descriptor.provider
        val enabled = pluginWrapper.pluginState == PluginState.STARTED
        val isHardwareFactory = pluginWrapper.plugin is HardwarePlugin
        return if (plugin is PluginMetadata) {
            val metadata = plugin as PluginMetadata
            val settingGroups = metadata.settingGroups.map { settingsGroupDtoMapper.map(it) }
            PluginDto(id, metadata.name, metadata.description, provider, version, isHardwareFactory, enabled, settingGroups)
        } else {
            PluginDto(id, R.plugin_no_name, R.plugin_no_description, provider, version, isHardwareFactory, enabled, null)
        }
    }
}