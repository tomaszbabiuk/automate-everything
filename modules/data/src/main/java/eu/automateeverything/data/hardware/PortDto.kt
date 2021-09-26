package eu.automateeverything.data.hardware

import eu.automateeverything.data.localization.Resource

data class PortDto(
    val id: String,
    val factoryId: String,
    val adapterId: String,
    val integerValue: Int?,
    val interfaceValue: Resource?,
    val valueClazz: String,
    val canRead: Boolean,
    val canWrite: Boolean,
    val connected: Boolean
)