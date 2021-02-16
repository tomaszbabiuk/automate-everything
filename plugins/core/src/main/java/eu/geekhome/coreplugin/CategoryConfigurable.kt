package eu.geekhome.coreplugin

import eu.geekhome.services.configurable.Configurable
import eu.geekhome.services.configurable.FieldDefinition
import eu.geekhome.services.localization.Resource

abstract class CategoryConfigurable : Configurable {

    override val addNewRes: Resource?
        get() = null

    override val editRes: Resource?
        get() = null

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() = mapOf()
}