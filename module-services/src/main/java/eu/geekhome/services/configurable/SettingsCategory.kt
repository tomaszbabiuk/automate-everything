package eu.geekhome.services.configurable

import eu.geekhome.services.localization.Resource
import org.pf4j.ExtensionPoint

interface SettingsCategory  : ExtensionPoint{
    val titleRes: Resource
    val descriptionRes: Resource
    val iconRaw: String
    val fieldDefinitions: Map<String, FieldDefinition<*>>
}