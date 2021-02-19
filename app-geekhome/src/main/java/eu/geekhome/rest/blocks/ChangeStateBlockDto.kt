package eu.geekhome.rest.blocks

import eu.geekhome.services.localization.Resource

data class ChangeStateBlockDto(
    val type: String,
    val message0: Resource,
    val previousStatement: String? = null,
    val nextStatement: String? = null,
    val colour: Int = 230,
    val tooltip: String? = null,
    val helpUrl: String? = null
)