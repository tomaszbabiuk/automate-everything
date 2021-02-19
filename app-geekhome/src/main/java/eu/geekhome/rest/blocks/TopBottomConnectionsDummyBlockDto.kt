package eu.geekhome.rest.blocks

import eu.geekhome.services.localization.Resource

open class BlockDto(
    val type: String,
    val colour: Int = 230,
    val tooltip: String? = null,
    val helpUrl: String? = null
)

class TopBottomConnectionsDummyBlockDto(
    val message0: Resource,
    val previousStatement: String? = null,
    val nextStatement: String? = null,
    type: String,
    colour: Int = 230,
    tooltip: String? = null,
    helpUrl: String? = null
) : BlockDto(type, colour, tooltip, helpUrl)

class LeftConnectionDummyBlockDto(
    val message0: Resource,
    val output: String? = null,
    type: String,
    colour: Int = 230,
    tooltip: String? = null,
    helpUrl: String? = null
) : BlockDto(type, colour, tooltip, helpUrl)