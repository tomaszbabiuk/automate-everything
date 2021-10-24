package eu.automateeverything.domain.extensibility

import eu.automateeverything.data.Repository
import eu.automateeverything.data.settings.SettingsDto

class SettingsExtractor(
    private val pluginsCoordinator: PluginsCoordinator,
    private val repository: Repository) {

    fun extractSettings(extensionClazz: Class<*>): List<SettingsDto> {
        val pluginContainingThisExtension = pluginsCoordinator.findExtensionOwner(extensionClazz)

        return extractSettings(pluginContainingThisExtension!!.pluginId)
    }

    fun extractSettings(pluginId: String): List<SettingsDto> {
        return pluginsCoordinator
            .getPluginSettingGroups(pluginId)
            .mapNotNull { repository.getSettingsByPluginIdAndClazz(pluginId, it.javaClass.name) }
    }
}