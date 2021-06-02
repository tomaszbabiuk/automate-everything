package eu.geekhome.rest.plugins

import eu.geekhome.R
import eu.geekhome.rest.PluginsCoordinatorHolderService
import eu.geekhome.rest.settinggroup.SettingGroupDtoMapper
import org.pf4j.PluginWrapper
import org.pf4j.PluginState
import eu.geekhome.services.hardware.HardwarePlugin
import eu.geekhome.services.extensibility.PluginMetadata
import javax.inject.Inject

class PluginDtoMapper @Inject constructor(
    private val settingsGroupDtoMapper: SettingGroupDtoMapper,
    pluginsCoordinatorHolderService: PluginsCoordinatorHolderService
) {
    val pluginsCoordinator = pluginsCoordinatorHolderService.instance

    fun map(pluginWrapper: PluginWrapper): PluginDto {
        val plugin = pluginWrapper.plugin
        val id = pluginWrapper.pluginId
        val version = pluginWrapper.descriptor.version
        val provider = pluginWrapper.descriptor.provider
        val enabled = pluginWrapper.pluginState == PluginState.STARTED
        val isHardwareFactory = pluginWrapper.plugin is HardwarePlugin
        val settingsCategories = pluginsCoordinator
            .findPluginSettingGroups(id)
            .map { settingsGroupDtoMapper.map(it)}
        return if (plugin is PluginMetadata) {
            val metadata = plugin as PluginMetadata
            PluginDto(id, metadata.name, metadata.description, provider, version, isHardwareFactory, enabled, settingsCategories)
        } else {
            PluginDto(id, R.plugin_no_name, R.plugin_no_description, provider, version, isHardwareFactory, enabled, null)
        }
    }
}