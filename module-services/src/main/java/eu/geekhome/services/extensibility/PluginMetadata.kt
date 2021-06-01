package eu.geekhome.services.extensibility

import eu.geekhome.services.localization.Resource

interface PluginMetadata {
    val name: Resource
    val description: Resource
}