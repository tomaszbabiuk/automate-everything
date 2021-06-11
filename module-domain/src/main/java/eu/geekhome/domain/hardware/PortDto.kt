package eu.geekhome.domain.hardware

import eu.geekhome.domain.localization.Resource

data class PortDto(
    val id: String,
    val factoryId: String,
    val adapterId: String,
    val integerValue: Int?,
    val interfaceValue: Resource?,
    val valueType: String,
    val canRead: Boolean,
    val canWrite: Boolean,
    val connected: Boolean
)