package eu.geekhome.rest.plugins

import org.pf4j.PluginWrapper
import org.pf4j.PluginState
import eu.geekhome.services.hardware.HardwarePlugin
import eu.geekhome.services.extensibility.PluginMetadata

class PluginDtoMapper {
    fun map(pluginWrapper: PluginWrapper): PluginDto {
        val plugin = pluginWrapper.plugin
        val id = pluginWrapper.pluginId
        val version = pluginWrapper.descriptor.version
        val provider = pluginWrapper.descriptor.provider
        val enabled = pluginWrapper.pluginState == PluginState.STARTED
        val isHardwareFactory = pluginWrapper.plugin is HardwarePlugin
        return if (plugin is PluginMetadata) {
            val metadata = plugin as PluginMetadata
            PluginDto(id, metadata.name, metadata.description, provider, version, isHardwareFactory, enabled)
        } else {
            PluginDto(id, null, null, provider, version, isHardwareFactory, enabled)
        }
    }
}