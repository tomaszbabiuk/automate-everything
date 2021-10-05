package eu.automateeverything.domain.extensibility

import eu.automateeverything.data.Repository
import eu.automateeverything.data.settings.SettingsDto

class ExtensionSettingsExtractor(
    private val pluginsCoordinator: PluginsCoordinator,
    private val repository: Repository) {

    fun extractPluginSettings(extensionClazz: Class<*>): List<SettingsDto> {
        val pluginContainingThisExtension = pluginsCoordinator.findExtensionOwner(extensionClazz)

        return pluginsCoordinator
            .getPluginSettingGroups(pluginContainingThisExtension!!)
            .mapNotNull { repository.getSettingsByPluginIdAndClazz(pluginContainingThisExtension.pluginId, it.javaClass.name) }
    }
}